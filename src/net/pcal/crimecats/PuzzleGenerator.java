package net.pcal.crimecats;

import java.io.PrintWriter;
import java.util.*;

public class PuzzleGenerator {

    private static final boolean DEBUG = true;

    public static void main(String[] args) {
        final PuzzleGenerator pg = new PuzzleGenerator();
        final Puzzle puzzle = pg.generate();
        final PrintWriter out = new PrintWriter(System.out);
        puzzle.printPuzzle(out);
        puzzle.printSolution(out);
        out.flush();
    }

    private final SolutionLUT allSolutions;
    private final Random random;
    private final List<MatchedClue> allClues;

    PuzzleGenerator() {
        allSolutions = SolutionLUT.NaiveSolutionLUT.create();
        random = new Random();
        allClues = generateMatchedClueLUT(allSolutions, RelativeClue.createRelativeClues());
    }

    public Puzzle generate() {
        while (true) {
            try {
                return generateOne(PuzzleDifficulty.INTERMEDIATE);
            } catch (IllegalStateException tryagain) {
            }
        }
    }

    private Puzzle generateOne(PuzzleDifficulty difficulty) {
        final int puzzleSolutionIndex = rand(allSolutions.getCount());
        final Set<MatchedClue> availableClues = new HashSet<>();
        for (final MatchedClue mc : this.allClues) {
            if (mc.getSolvedSolutions().get(puzzleSolutionIndex)) {
                availableClues.add(mc);
            }
        }
        debug("Solution Index = " + puzzleSolutionIndex + ", " + availableClues.size() + " matching clues identified");
        final Position crime = Position.values()[rand(Position.values().length)];
        final List<MatchedClue> puzzleClues = new ArrayList<>();
        final int SAFETY_LIMIT = 10000;
        final int clueCount = difficulty.getClueCount();
        int safety = 0;
        for (int i = 0; i < clueCount; i++) {
            if (i == 0) {
                final MatchedClue clue = getRandomClue(availableClues, difficulty.getClueDifficulty(i));
                puzzleClues.add(clue);
            } else {
                final BitSet currentSols = getSolutions(puzzleClues);
                while (true) {
                    removeInvalidCandidates(puzzleClues, availableClues);
                    final MatchedClue clue = getRandomClue(availableClues, difficulty.getClueDifficulty(i));
                    debug("cardinality=" + clue.getSolvedSolutions().cardinality() + " '" + clue.getClue() + "'");
                    puzzleClues.add(clue);
                    final BitSet proposedSols = ((BitSet) currentSols.clone());
                    proposedSols.and(clue.getSolvedSolutions());
                    if (proposedSols.cardinality() > 0) {
                        if (i < clueCount - 1 && proposedSols.cardinality() < currentSols.cardinality() / 2 && proposedSols.cardinality() > currentSols.cardinality() / 4) {
                            debug("i=" + i + " clue chosen, test cardinality was " + proposedSols.cardinality());
                            break;
                        } else if (i == clueCount - 1 && proposedSols.cardinality() == 1) {
                            debug("i=" + i + " clue chosen, test cardinality was " + proposedSols.cardinality());
                            break;
                        }
                    }
                    if (safety++ > SAFETY_LIMIT) {
                        System.out.println("hit safety limit, retrying");
                        throw new IllegalStateException("failed to find a solution");
                    }
                    puzzleClues.remove(clue);
                    debug("i=" + i + " picking a new clue, proposed cardinality was " + proposedSols.cardinality());
                }
            }
        }
        final List<Clue> finalPuzzleClues = new ArrayList<>();
        for (MatchedClue mc : puzzleClues) {
            finalPuzzleClues.add(mc.getClue());
        }
        return new Puzzle(crime, this.allSolutions.getSolution(puzzleSolutionIndex), finalPuzzleClues);
    }

    private MatchedClue getRandomClue(final Set<MatchedClue> clues, final PuzzleDifficulty.ClueDifficulty d) {
        final Iterator<MatchedClue> i = clues.iterator();
        int n = rand(clues.size());
        while(--n > 0) {
            i.next();
        }
        return i.next();
    }

    private static void removeInvalidCandidates(List<MatchedClue> currentClues, Set<MatchedClue> remainingClues) {
        final BitSet allSolved = getSolutions(currentClues);
        final BitSet[] exclusives = getSolitionsExclusiveOfEach(currentClues);
        final Iterator<MatchedClue> r = remainingClues.iterator();
        outer:
        while(r.hasNext()) {
            final MatchedClue remaining = r.next();
            final BitSet remainingUnique = (BitSet)remaining.getSolvedSolutions().clone();
            remainingUnique.(allSolved);
            if (remainingUnique.isEmpty()) {
                debug("removing '"+remaining+" because it doesn't constrain the solution set");
                r.remove();
                break outer;
            }
            for (int i = 0; i < exclusives.length; i++) {
                final BitSet exclusive = (BitSet) exclusives[i].clone();
                exclusive.andNot(remaining.getSolvedSolutions());
                if (exclusive.isEmpty()) {
                    debug("removing '"+remaining+" because it obviates '"+currentClues.get(i)+"'");
                    r.remove();
                    break outer;
                }
            }
        }
    }

    private static BitSet getSolutions(List<MatchedClue> clues) {
        final BitSet out = (BitSet) clues.get(0).getSolvedSolutions().clone();
        for (int i = 1; i < clues.size(); i++) {
            out.and(clues.get(i).getSolvedSolutions());
        }
        return out;
    }

    // return the solutions there are uniquely identified by each of the clues so far
    private static BitSet[] getSolitionsExclusiveOfEach(List<MatchedClue> clues) {
        final BitSet[] exclusives = new BitSet[clues.size()];
        for (int i = 0; i < clues.size(); i++) {
            exclusives[i] = (BitSet)clues.get(i).getSolvedSolutions().clone();
            for (int j = 0; j < clues.size(); j++) {
                if (i != j) exclusives[i].and(clues.get(j).getSolvedSolutions());
            }
        }
        return exclusives;
    }

    private int rand(int max) {
        return random.nextInt(max);
    }

    private static void debug(String msg) {
        if (DEBUG) System.out.println(msg);
    }

    private static List<MatchedClue> generateMatchedClueLUT(final SolutionLUT allSolutions, final List<? extends Clue> allClues) {
        final long start = System.currentTimeMillis();
        final List<MatchedClue> out = new ArrayList<>();
        for (final Clue clue : allClues) {
            final BitSet bs = new BitSet();
            for (int i = 0; i < allSolutions.getCount(); i++) {
                if (clue.matches(allSolutions.getSolution(i))) bs.set(i);
            }
            out.add(new MatchedClue(clue, bs));
        }
        Collections.sort(out);
        for (int i = 0; i < out.size(); i++) {
            MatchedClue mc = out.get(i);
            debug(i + " cardinality=" + mc.getSolvedSolutions().cardinality() + " - " + mc.clue.toString());
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Precached matches for " + allClues.size() + " clues in " + duration + "ms");
        return out;
    }

    private static class MatchedClue implements Comparable<MatchedClue> {

        private final Clue clue;
        private final BitSet solvedSolutions;

        public MatchedClue(Clue clue, BitSet solvedSolutions) {
            this.clue = clue;
            this.solvedSolutions = solvedSolutions;
        }

        public BitSet getSolvedSolutions() {
            return this.solvedSolutions;
        }

        public Clue getClue() {
            return this.clue;
        }

        @Override
        public int hashCode() {
            return this.clue.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof MatchedClue && this.clue.equals(((MatchedClue) o).clue);
        }

        @Override
        public int compareTo(MatchedClue o) {
            return Integer.compare(this.solvedSolutions.cardinality(), o.solvedSolutions.cardinality());
        }

        @Override
        public String toString() {
            return this.clue.toString();
        }
    }
}
