package net.pcal.crimecats;

import java.util.EnumSet;

import static net.pcal.crimecats.Position.*;

public interface Preposition {

    EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions);

    default boolean canHaveCatTarget() {
        return true;
    }

    String getDescription();

    static enum PrepositionImpl implements Preposition {

        AT() {

            @Override
            public EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                return subjectPositions;
            }

            @Override
            public String getDescription() {
                return "was sitting in front of";
            }

            @Override
            public boolean canHaveCatTarget() {
                return false;
            }

        },

        NOT_AT() {
            @Override
            public EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                return EnumSet.complementOf(subjectPositions);
            }

            @Override
            public String getDescription() {
                return "was not sitting in front of";
            }

            @Override
            public boolean canHaveCatTarget() {
                return false;
            }
        },

        LEFT_OF() {
            @Override
            public EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                EnumSet<Position> out = EnumSet.noneOf(Position.class);
                for(Position p : subjectPositions) {
                    out.add(p.offset(LEFT_OFFSET));
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was sitting to the left of";
            }
        },

        NOT_LEFT_OF() {
            @Override
            public  EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                EnumSet<Position> out = EnumSet.allOf(Position.class);
                for(Position p : subjectPositions) {
                    out.remove(p.offset(LEFT_OFFSET));
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was not sitting to the left of";
            }
        },

        RIGHT_OF() {
            @Override
            public  EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                EnumSet<Position> out = EnumSet.noneOf(Position.class);
                for(Position p : subjectPositions) {
                    out.add(p.offset(RIGHT_OFFSET));
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was sitting to the right of";
            }
        },

        NOT_RIGHT_OF() {
            @Override
            public  EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                EnumSet<Position> out = EnumSet.allOf(Position.class);
                for(Position p : subjectPositions) {
                    out.remove(p.offset(RIGHT_OFFSET));
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was not sitting to the right of";
            }
        },

        NEXT_TO() {
            @Override
            public  EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                EnumSet<Position> out = EnumSet.noneOf(Position.class);
                for(Position p : subjectPositions) {
                    out.add(p.offset(1));
                    out.add(p.offset(-1));
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was sitting next to";
            }
        },

        NOT_NEXT_TO() {
            @Override
            public  EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                EnumSet<Position> out = EnumSet.allOf(Position.class);
                for(Position p : subjectPositions) {
                    out.remove(p.offset(1));
                    out.remove(p.offset(-1));
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was not sitting next to";
            }
        },

        TWO_AWAY_FROM() {
            @Override
            public EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                EnumSet<Position> out = EnumSet.noneOf(Position.class);
                for(Position p : subjectPositions) {
                    out.add(p.offset(2));
                    out.add(p.offset(-2));
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was sitting 2 seats from";
            }

        },

        THREE_AWAY_FROM() {
            @Override
            public EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                EnumSet<Position> out = EnumSet.noneOf(Position.class);
                for(Position p : subjectPositions) {
                    out.add(p.offset(3));
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was sitting 3 seats from";
            }
        },

        ACROSS_FROM() {
            @Override
            public EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                final EnumSet<Position> out = EnumSet.noneOf(Position.class);
                for(Position p : subjectPositions) {
                    out.add(p.getAcross());
                }
                return out;
            }
            @Override
            public String getDescription() {
                return "was sitting across from";
            }
        },

        NOT_ACROSS_FROM() {
            @Override
            public EnumSet<Position> getPossibilities(EnumSet<Position> subjectPositions) {
                final EnumSet<Position> out = EnumSet.allOf(Position.class);
                for(Position p : subjectPositions) {
                    out.remove(p.getAcross());
                }
                return out;
            }

            @Override
            public String getDescription() {
                return "was not sitting across from";
            }
        };
    }
}
