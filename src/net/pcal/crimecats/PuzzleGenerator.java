package net.pcal.crimecats;

import java.io.PrintWriter;
import java.util.*;

public class PuzzleGenerator {

    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        final PuzzleGenerator pg = new PuzzleGenerator();
        final Puzzle puzzle = pg.generate(PuzzleDifficulty.INTERMEDIATE);
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

    public Puzzle generate(PuzzleDifficulty difficulty) {
        while (true) {
            try {
                return generateOne(difficulty);
            } catch (IllegalStateException tryagain) {
                System.out.println("...still thinking...");
            }
        }
    }

    private Puzzle generateOne(PuzzleDifficulty difficulty) {
        final int solutionCount = allSolutions.getCount();
        final int puzzleSolutionIndex = rand(solutionCount);
        final Set<MatchedClue> availableClues = new HashSet<>();
        for (final MatchedClue mc : this.allClues) {
            if (!mc.getSolvedSolutions().get(puzzleSolutionIndex)) {
                // filter out any clues that don't match the solution
                continue;
            }
            availableClues.add(mc);
        }
        debug("Solution Index = " + puzzleSolutionIndex + ", " + availableClues.size() + " matching clues identified");
        final Position crime = Position.values()[rand(Position.values().length)];
        final List<MatchedClue> puzzleClues = new ArrayList<>();
        final int SAFETY_LIMIT = 10000;
        final int clueCount = difficulty.getClueCount();
        int safety = 0;
        for (int i = 0; i < clueCount; i++) {
            if (i == 0) {
                final MatchedClue clue = getRandomClue(solutionCount, availableClues, difficulty.getClueDifficulty(i));
                puzzleClues.add(clue);
            } else {
                final BitSet currentSols = getSolutions(puzzleClues);
                while (true) {
                    removeInvalidCandidates(puzzleClues, availableClues);
                    final MatchedClue clue = getRandomClue(solutionCount, availableClues, difficulty.getClueDifficulty(i));
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

    private MatchedClue getRandomClue(final int solutionCount, final Set<MatchedClue> cluesOriginal, final PuzzleDifficulty.ClueDifficulty d) {
        final List<MatchedClue> clues = new ArrayList<>(cluesOriginal);
        Collections.shuffle(clues);
        final Iterator<MatchedClue> i = clues.iterator();
        while (i.hasNext()) {
            final MatchedClue clue = i.next();
            if (!d.matches(clue.getSolvedSolutions().cardinality(), solutionCount)) {
                i.remove();
            }
        }
        if (clues.size() == 0) {
            throw new IllegalStateException("could not find valid clue");
        }
        return clues.get(rand(clues.size()));
    }

    private static void removeInvalidCandidates(List<MatchedClue> currentPuzzleClues, Set<MatchedClue> candidates) {
        final BitSet currentSolutions = getSolutions(currentPuzzleClues);
        final int currentCardinality = currentSolutions.cardinality();
        final BitSet[] exclusives = getUniqueSolutionsForEach(currentPuzzleClues);
        final Iterator<MatchedClue> ci = candidates.iterator();
        outer:
        while (ci.hasNext()) {
            final MatchedClue candidate = ci.next();
            final BitSet solutionsWithCandidate = (BitSet) currentSolutions.clone();
            solutionsWithCandidate.andNot(candidate.getSolvedSolutions());
            if (solutionsWithCandidate.cardinality() == currentCardinality) {
                // if adding the clue to the current puzzle doesn't reduce the cardinality of the solution set
                // then it's never going to contribute anything, so let's not bother with it anymore
                debug("removing '" + candidate + " because it doesn't constrain the solution set");
                ci.remove();
                break outer;
            }
            for (int i = 0; i < exclusives.length; i++) {
                final BitSet candidateSolutions = (BitSet) candidate.getSolvedSolutions().clone();
                candidateSolutions.andNot(exclusives[i]);
                if (candidateSolutions.isEmpty()) {
                    // If the solutions matched by the candidate clue are a subset of the solutions uniquely matched
                    // by one of the puzzle clues, then that candidate rules out all of the solutions that the
                    // already-chosen clue does, rendering that clue useless.  In the case, let's also discard
                    // the candidate from further  consideration.
                    debug("removing '" + candidate + " because it obviates '" + currentPuzzleClues.get(i) + "'");
                    ci.remove();
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
    private static BitSet[] getUniqueSolutionsForEach(List<MatchedClue> clues) {
        final BitSet[] exclusives = new BitSet[clues.size()];
        for (int i = 0; i < clues.size(); i++) {
            exclusives[i] = (BitSet) clues.get(i).getSolvedSolutions().clone();
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
