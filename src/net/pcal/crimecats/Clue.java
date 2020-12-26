package net.pcal.crimecats;

import static net.pcal.crimecats.Preposition.*;

public interface Clue {

    boolean matches(Solution solution);

    static class ClueImpl implements Clue {

        private CatClue catClue;
        private Preposition prep;
        private PositionClue posClue;

        @Override
        public boolean matches(final Solution solution) {
            switch(this.prep) {
                case AT:
                    for (PositionClue.Position pos : PositionClue.Position.values()) {
                        for (Cat cat : this.catClue.getPossibleCats()) {
                            if (solution.getCatAt(pos) == cat) {
                                return true;
                            }
                        }
                    }
                    return false;
                case NOT_AT:
                    for (PositionClue.Position pos : PositionClue.Position.values()) {
                        for (Cat cat : this.catClue.getPossibleCats()) {
                            if (solution.getCatAt(pos) == cat) {
                                return false;
                            }
                        }
                    }
                    return true;
                case LEFT_OF:
                case RIGHT_OF:
                case TWO_AWAY_FROM:
                case THREE_AWAY_FROM:
                case ACROSS_FROM:
                    throw new IllegalStateException();
            }

            return false;
        }
    }

}
