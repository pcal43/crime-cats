package net.pcal.crimecats;

import static net.pcal.crimecats.PuzzleDifficulty.ClueDifficulty.*;

public enum PuzzleDifficulty {

    BEGINNER("Beginner", EASY, EASY, EASY, EASY, EASY),
    INTERMEDIATE("Intermediate", EASY, MEDIUM, EASY, MEDIUM, EASY, MEDIUM);
//    ADVANCED("Advanced",new int[][] {{0, 120}}),
//    EXPERT("Expert",new int[][] {{0, 120}});

    private final String label;
    private final ClueDifficulty[] clueRanges;

    PuzzleDifficulty(final String label, ClueDifficulty... clueRanges){
        this.label = label;
        this.clueRanges = clueRanges;
    }

    public int getClueCount() {
        return clueRanges.length;
    }

    public ClueDifficulty getClueDifficulty(int i) {
        return clueRanges[i];
    }

    public enum ClueDifficulty {
        EASY(0, 240),
        MEDIUM(121, 520),
        HARD(340, 720);

        private final int min, max;

        ClueDifficulty(int min, int max) {
            this.min = min;
            this.max = max;
        }
        
        boolean matches(int cardinality) {
            return min <= cardinality && cardinality <= max;
        }
    }
}
