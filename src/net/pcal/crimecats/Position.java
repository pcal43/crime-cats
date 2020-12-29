package net.pcal.crimecats;

import java.util.EnumSet;

public enum Position {

    BIRD(0, "the birdcage", "Who ate the bird?"),
    COFFEE(1, "the coffee", "Who spilled the coffee?"),
    SHOES(2, "the shoes", "Who ruined the shoes?"),
    FISH(3, "the fish bowl", "Who swallowed the fish?"),
    YARN(4, "the ball of yarn", "Who unraveled the yarn?"),
    FLOWER(5, "the flower pot", "Who broke the flower pot?");

    private static final Position[] ACROSS = {
            FISH,
            FLOWER,
            YARN,
            BIRD,
            SHOES,
            COFFEE
    };

    public static final int LEFT_OFFSET = -1;
    public static final int RIGHT_OFFSET = 1;

    private final String description;
    private final int pos;
    private  EnumSet<Position> array;
    private final String crime;

    private Position(int pos, String description, String crime) {
        this.pos = pos;
        this.description = description;
        this.crime = crime;
    }

    public String getDescription() {
        return this.description;
    }
    
    public String getCrime() {
        return this.crime;
    }

    public int getIndex() {
        return this.pos;
    }

    public EnumSet<Position> asArray() {
        if (this.array == null) {
            this.array = EnumSet.of(this);
        }
        return this.array;
    }

    public Position getAcross() {
        return ACROSS[this.ordinal()];
    }

    public Position offset(int by) {
        return Position.values()[Math.floorMod((this.ordinal() + by), Position.values().length)];
    }

}
