package net.pcal.crimecats;

import java.io.PrintWriter;
import java.util.List;

public class Puzzle {

    private final Position crime;
    private final Solution solution;
    private final List<Clue> clues;

    public Puzzle(Position crime, Solution solution, List<Clue> clues) {
        this.crime = crime;
        this.solution = solution;
        this.clues = clues;
    }

    public List<Clue> getClues() {
        return clues;
    }

    public Solution getSolution() {
        return solution;
    }

    public void printPuzzle(PrintWriter pw) {
        pw.println("----------------------------------");
        pw.println(this.crime.getCrime());
        pw.println("----------------------------------");
        for (int i = 0; i < this.clues.size(); i++) {
            pw.println("* " + clues.get(i).toString());
        }
    }

    public void printSolution(PrintWriter pw) {
        Cat guilty = this.solution.getCatAt(this.crime);
        pw.println("----------------------------------");
        pw.println("The guilty cat is..." + guilty.getDescription() + "!");
        pw.println("----------------------------------");
        this.solution.print(pw);
    }
}
