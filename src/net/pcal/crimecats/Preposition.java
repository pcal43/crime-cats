package net.pcal.crimecats;

import net.pcal.crimecats.PositionClue.Position;

import static net.pcal.crimecats.PositionClue.Position.*;

public interface Preposition {

    Position[] getPossibilities(Position pos);

    String getDescription();

    static enum PrepositionImpl implements Preposition {

        AT() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.asArray();
            }
        },

        LEFT_OF() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.offset(LEFT_OFFSET).asArray();
            }
        },

        RIGHT_OF() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.offset(RIGHT_OFFSET).asArray();
            }
        },

        NEXT_TO() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return new Position[] {pos.offset(1), pos.offset(-1) };
            }
        },

        TWO_AWAY_FROM() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return new Position[] {pos.offset(2), pos.offset(-2) };
            }
        },

        THREE_AWAY_FROM() {
            @Override
            public Position[] getPossibilities(Position pos) {
                return pos.offset(3).asArray();
            }
        },

        ACROSS_FROM() {
            @Override
            public Position[] getPossibilities(Position pos) {
                switch (pos) {
                    case ONE:
                        return FOUR.asArray();
                    case TWO:
                        return SIX.asArray();
                    case THREE:
                        return FIVE.asArray();
                    case FOUR:
                        return ONE.asArray();
                    case FIVE:
                        return THREE.asArray();
                    case SIX:
                        return TWO.asArray();
                    default:
                        throw new IllegalStateException();
                }
            }
        };

        @Override
        public String getDescription() {
            return this.name();
        }
    }

}
