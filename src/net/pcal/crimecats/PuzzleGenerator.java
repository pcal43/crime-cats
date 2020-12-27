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

    PuzzleGenerator() {
        allSolutions = SolutionLUT.NaiveSolutionLUT.create();
        allClues = RelativeClue.createRelativeClues();
        random = new Random();
        puzzleSolution = allSolutions.getSolution(rand(allSolutions.getCount()));
    }

    private static void debug(String msg) {
        //System.out.println(msg);
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
                final Collection<Solution> currentSols = getAllSolutionsMatchedBy(puzzleClues);
                while (true) {
                    if (safety++ > SAFETY_LIMIT) {
                        debug("hit safety limit, bailing");
                        throw new IllegalStateException("failed to find a solution");
                    }
                    puzzleClues.add(clue);
                    final Collection<Solution> proposedSols = getAllSolutionsMatchedBy(puzzleClues);
                    if (proposedSols.size() > 0) {
                        if (i < CLUE_COUNT - 1 && proposedSols.size() < currentSols.size() / 2 && proposedSols.size() > currentSols.size() / 4) {
                            debug("i=" + i + " clue chosen, test size was " + proposedSols.size());
                            break;
                        } else if (i == CLUE_COUNT - 1 && proposedSols.size() == 1) {
                            debug("i=" + i + " clue chosen, test size was " + proposedSols.size());
                            break;
                        }
                    }
                    puzzleClues.remove(clue);
                    debug("i=" + i + " picking a new clue, test size was " + proposedSols.size());
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

    private Set<Solution> getAllSolutionsMatchedBy(List<Clue> clues) {
        final Set<Solution> out = new HashSet<>();
        for (int i = 0; i < this.allSolutions.getCount(); i++) {
            final Solution sol = this.allSolutions.getSolution(i);
            if (matchesAll(sol, clues)) out.add(sol);
        }
        return out;
    }

    private boolean matchesAll(Solution sol, List<Clue> clues) {
        for (Clue clue : clues) {
            if (!clue.matches(sol)) return false;
        }
        return true;
    }

    private int rand(int max) {
        return random.nextInt(max);
    }

}
