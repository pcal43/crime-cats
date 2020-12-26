package net.pcal.crimecats;


public interface PositionClue {

    String getDescription();

    Position[] getPossiblePositions(Solution solution);

    public enum Position implements PositionClue {

        ONE(1, "Bird cage"),
        TWO(2, "Coffee"),
        THREE(3, "Shoes"),
        FOUR(4, "Aquarium"),
        FIVE(5, "Ball of yarn"),
        SIX(6, "Flower pot");

        private final String description;
        private final int pos;
        private final Position[] possibles;

        private Position(int pos, String description) {
            this.pos = pos;
            this.description = description;
            this.possibles = new Position[] { this };
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public Position[] getPossiblePositions(Solution solution) {
            return this.possibles;
        }

        public int getIndex() {
            return this.pos;
        }

        public Position[] asArray() {
            return this.possibles;
        }
    }

}