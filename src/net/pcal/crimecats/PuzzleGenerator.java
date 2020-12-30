package net.pcal.crimecats;

import java.io.PrintWriter;
import java.util.*;

public class PuzzleGenerator {

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
                return generateOne();
            } catch (IllegalStateException tryagain) {
            }
        }
    }

    private Puzzle generateOne() {
        final int puzzleSolutionIndex = rand(allSolutions.getCount());
        final List<MatchedClue> matchingClues = new ArrayList<>();
        for (final MatchedClue mc : this.allClues) {
            if (mc.getSolvedSolutions().get(puzzleSolutionIndex)) {
                matchingClues.add(mc);
            }
        }
        debug("Solution Index = " + puzzleSolutionIndex + ", " + matchingClues.size() + " matching clues identified");
        final Position crime = Position.values()[rand(Position.values().length)];
        final List<MatchedClue> puzzleClues = new ArrayList<>();
        final int CLUE_COUNT = 6;
        final int SAFETY_LIMIT = 10000;
        int safety = 0;
        for (int i = 0; i < CLUE_COUNT; i++) {
            if (i == 0) {
                final MatchedClue clue = matchingClues.get(rand(matchingClues.size()));
                puzzleClues.add(clue);
            } else {
                final BitSet currentSols = (BitSet)puzzleClues.get(0).getSolvedSolutions().clone();
                for(int j=1; j<puzzleClues.size(); j++) {
                    currentSols.and(puzzleClues.get(j).getSolvedSolutions());
                }
                while (true) {
                    final MatchedClue clue = matchingClues.get(rand(matchingClues.size()));
                    debug("cardinality=" + clue.getSolvedSolutions().cardinality() + " '" + clue.getClue() + "'");
                    puzzleClues.add(clue);
                    final BitSet proposedSols = ((BitSet) currentSols.clone());
                    proposedSols.and(clue.getSolvedSolutions());
                    if (proposedSols.cardinality() > 0) {
                        if (i < CLUE_COUNT - 1 && proposedSols.cardinality() < currentSols.cardinality() / 2 && proposedSols.cardinality() > currentSols.cardinality() / 4) {
                            debug("i=" + i + " clue chosen, test cardinality was " + proposedSols.cardinality());
                            break;
                        } else if (i == CLUE_COUNT - 1 && proposedSols.cardinality() == 1) {
                            debug("i=" + i + " clue chosen, test cardinality was " + proposedSols.cardinality());
                            break;
                        }
                    }
                    if (safety++ > SAFETY_LIMIT) {
                        debug("hit safety limit, bailing");
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

    private int rand(int max) {
        return random.nextInt(max);
    }

    private static void debug(String msg) {
        System.out.println(msg);
    }

    private static List<MatchedClue> generateMatchedClueLUT(final SolutionLUT allSolutions, final List<? extends Clue> allClues) {
        final long start = System.currentTimeMillis();
        final List<MatchedClue> out = new ArrayList<>();
        for(final Clue clue : allClues) {
            final BitSet bs = new BitSet();
            for (int i = 0; i < allSolutions.getCount(); i++) {
                if (clue.matches(allSolutions.getSolution(i))) bs.set(i);
            }
            out.add(new MatchedClue(clue, bs));
        }
        Collections.sort(out);
        for(int i=0; i<out.size(); i++) {
            MatchedClue mc = out.get(i);
            debug(i+" cardinality="+mc.getSolvedSolutions().cardinality()+" - "+mc.clue.toString());
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Precached matches for "+allClues.size() + " clues in "+duration+"ms");
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
            return o instanceof MatchedClue && this.clue.equals(((MatchedClue)o).clue);
        }

        @Override
        public int compareTo(MatchedClue o) {
            return Integer.compare(this.solvedSolutions.cardinality(), o.solvedSolutions.cardinality());
        }
    }
}
