package net.pcal.crimecats;

import static net.pcal.crimecats.Position.*;

public interface Preposition {

    Position[] getPossibilities(Position pos);

    String getDescription();

    static enum PrepositionImpl implements Preposition {

        AT() {

            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.asArray();
            }

            @Override
            public String getDescription() {
                return "was sitting in front of";
            }
        },

        NOT_AT() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.getInversion();
            }

            @Override
            public String getDescription() {
                return "was not sitting in front of";
            }
        },

        LEFT_OF() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.offset(LEFT_OFFSET).asArray();
            }
            @Override
            public String getDescription() {
                return "was sitting to the left of";
            }
        },

        NOT_LEFT_OF() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.offset(LEFT_OFFSET).getInversion();
            }
            @Override
            public String getDescription() {
                return "was not sitting to the left of";
            }
        },

        RIGHT_OF() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.offset(RIGHT_OFFSET).asArray();
            }
            @Override
            public String getDescription() {
                return "was sitting to the right of";
            }
        },

        NOT_RIGHT_OF() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.offset(RIGHT_OFFSET).getInversion();
            }
            @Override
            public String getDescription() {
                return "was not sitting to the right of";
            }
        },

        NEXT_TO() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return new Position[] {pos.offset(1), pos.offset(-1) };
            }
            @Override
            public String getDescription() {
                return "was sitting next to";
            }
        },

        TWO_AWAY_FROM() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return new Position[] {pos.offset(2), pos.offset(-2) };
            }
            @Override
            public String getDescription() {
                return "was sitting 2 seats from";
            }

        },

        THREE_AWAY_FROM() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.offset(3).asArray();
            }
            @Override
            public String getDescription() {
                return "was sitting 3 seats from";
            }

        },

        ACROSS_FROM() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.getAcross().asArray();
            }
            @Override
            public String getDescription() {
                return "was sitting across from";
            }
        },

        NOT_ACROSS_FROM() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.getAcross().getInversion();
            }

            @Override
            public String getDescription() {
                return "was not sitting across from";
            }
        };

    }

}
