package net.pcal.crimecats;

import java.io.PrintWriter;
import java.util.*;

public class PuzzleGenerator {

    public static void main(String[] args) {
        PuzzleGenerator pg = new PuzzleGenerator();
        Puzzle puzzle = pg.generate();
        PrintWriter out = new PrintWriter(System.out);
        puzzle.printPuzzle(out);
        puzzle.printSolution(out);
        out.flush();
    }

    private final SolutionLUT allSolutions;
    private final List<RelativeClue> allClues;
    private final Random random;
    private final Solution puzzleSolution;
    private final ClueMatcher clueMatcher;

    PuzzleGenerator() {
        allSolutions = SolutionLUT.NaiveSolutionLUT.create();
        allClues = RelativeClue.createRelativeClues();
        random = new Random();
        puzzleSolution = allSolutions.getSolution(rand(allSolutions.getCount()));
        clueMatcher = new ClueMatcher(allSolutions);
    }

    private static void debug(String msg) {
        System.out.println(msg);
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
        final Position crime = Position.values()[rand(Position.values().length)];
        final List<Clue> puzzleClues = new ArrayList<>();
        final int CLUE_COUNT = 6;
        final int SAFETY_LIMIT = 10000;
        int safety = 0;
        for (int i = 0; i < CLUE_COUNT; i++) {
            Clue clue = pickRandomClue();
            if (i == 0) {
                puzzleClues.add(clue);
            } else {
                final BitSet currentSols = this.clueMatcher.getSolutionsMatchedByAll(puzzleClues);
                while (true) {
                    if (safety++ > SAFETY_LIMIT) {
                        debug("hit safety limit, bailing");
                        throw new IllegalStateException("failed to find a solution");
                    }
                    final BitSet clueSols = this.clueMatcher.getSolutionsMatchedBy(clue);
                    debug("cardinality="+clueSols.cardinality()+" '"+clue+"'");

                    puzzleClues.add(clue);
                    final BitSet proposedSols = this.clueMatcher.getSolutionsMatchedByAll(puzzleClues);
                    if (proposedSols.cardinality() > 0) {
                        if (i < CLUE_COUNT - 1 && proposedSols.cardinality() < currentSols.cardinality() / 2 && proposedSols.cardinality() > currentSols.cardinality() / 4) {
                            debug("i=" + i + " clue chosen, test cardinality was " + proposedSols.cardinality());
                            break;
                        } else if (i == CLUE_COUNT - 1 && proposedSols.cardinality() == 1) {
                            debug("i=" + i + " clue chosen, test cardinality was " + proposedSols.cardinality());
                            break;
                        }
                    }
                    puzzleClues.remove(clue);
                    debug("i=" + i + " picking a new clue, test cardinality was " + proposedSols.cardinality());
                    clue = pickRandomClue();
                }
            }
        }
        return new Puzzle(crime, this.puzzleSolution, puzzleClues);
    }

    private Clue pickRandomClue() {
        while (true) {
            final Clue clue = allClues.get(rand(allClues.size()));
            if (clue.matches(this.puzzleSolution)) {
                return clue;
            }
        }
    }

    private static class ClueMatcher {

        private Map<Clue, BitSet> matchCache = new HashMap<>();
        private SolutionLUT allSolutions;

        ClueMatcher(SolutionLUT allSolutions) {
            this.allSolutions = allSolutions;
        }

        private BitSet getSolutionsMatchedByAll(List<Clue> clues) {
            BitSet out = null;
            for(Clue  clue : clues) {
                if (out == null) {
                    out = (BitSet)getSolutionsMatchedBy(clue).clone();
                } else {
                    out.and(getSolutionsMatchedBy(clue));
                }
            }
            return out;
        }

        private BitSet getSolutionsMatchedBy(Clue clue) {
            BitSet out = this.matchCache.get(clue);
            if (out != null) {
                return out;
            } else {
                out = new BitSet(allSolutions.getCount());
                for (int i = 0; i < allSolutions.getCount(); i++) {
                    if (clue.matches(allSolutions.getSolution(i))) out.set(i);
                }
                this.matchCache.put(clue, out);
            }
            return out;
        }
    }

    private int rand(int max) {
        return random.nextInt(max);
    }

}
