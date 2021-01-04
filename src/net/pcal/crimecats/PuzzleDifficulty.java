package net.pcal.crimecats;

import static net.pcal.crimecats.PuzzleDifficulty.ClueDifficulty.*;

public enum PuzzleDifficulty {

    BEGINNER("Beginner", EASY, EASY, EASY, EASY, EASY),
    INTERMEDIATE("Intermediate", EASY, MEDIUM, EASY, MEDIUM, EASY, MEDIUM),
    ADVANCED("Advanced", MEDIUM, MEDIUM, HARD, MEDIUM, MEDIUM, HARD),
    EXPERT("Expert", HARD, HARD, HARD, HARD, HARD, HARD, HARD);

    private final String label;
    private final ClueDifficulty[] clueRanges;

    PuzzleDifficulty(final String label, ClueDifficulty... clueRanges) {
        this.label = label;
        this.clueRanges = clueRanges;
    }

    public String getLabel() {
        return this.label;
    }

    public int getClueCount() {
        return clueRanges.length;
    }

    public ClueDifficulty getClueDifficulty(int i) {
        return clueRanges[i];
    }

    public enum ClueDifficulty {
        EASY(0, 220),
        MEDIUM(121, 520),
        HARD(340, 720);

        private final int min, max;

        ClueDifficulty(int min, int max) {
            this.min = min;
            this.max = max;
        }

        boolean matches(int cardinality, int totalSolutions) {
            return min <= cardinality && cardinality <= max;
        }
    }
}
