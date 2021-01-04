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
            PuzzleDifficulty difficulty;
            while(true) {
                out.print("\033[H\033[2J"); // clear screen
                out.println("Enter difficulty:");
                for(final PuzzleDifficulty d : PuzzleDifficulty.values()) {
                    out.println(" "+(d.ordinal()+1)+") "+d.getLabel());
                }
                out.println(" 0) Quit");
                try {
                    int selected = scanner.nextInt();
                    if (selected == 0) {
                        System.out.println("Bye!");
                        System.exit(0);
                    }
                    difficulty = PuzzleDifficulty.values()[selected - 1];
                    break;
                } catch(Exception e) {
                    out.println("try again\n4");
                } finally {
                    scanner.nextLine();
                }
            }
            out.println();
            out.println("Generating "+difficulty.getLabel()+" puzzle...");
            out.flush();
            final Puzzle puzzle = pg.generate(difficulty);
            out.println();
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
