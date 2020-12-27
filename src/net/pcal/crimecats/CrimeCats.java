package net.pcal.crimecats;

import java.io.PrintWriter;
import java.util.Scanner;

public class CrimeCats {

    public static void main(String[] args) {
        final PrintWriter out = new PrintWriter(System.out, true);
        final Scanner scanner = new Scanner(System.in);
        out.println("--------------------------");
        out.println("  Welcome to Crime Cats!  ");
        out.println("--------------------------");
        out.println();
        PuzzleGenerator pg = new PuzzleGenerator();
        while(true) {
            out.println("Generating puzzle...");
            out.flush();
            Puzzle puzzle = pg.generate();
            out.print("\033[H\033[2J");
            puzzle.printPuzzle(out);
            out.println();
            out.println("Press Enter to see the answer...");
            scanner.nextLine();
            puzzle.printSolution(out);
            out.println();
            out.println("Press Enter for a new puzzle...");
            out.flush();
            scanner.nextLine();
            out.flush();
        }
    }


}
