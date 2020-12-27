package net.pcal.crimecats;

public enum Position  {

    ONE(1, "Bird cage"),
    TWO(2, "Coffee"),
    THREE(3, "Shoes"),
    FOUR(4, "Aquarium"),
    FIVE(5, "Ball of yarn"),
    SIX(6, "Flower pot");

    public static final int LEFT_OFFSET = -1;
    public static final int RIGHT_OFFSET = 1;

    private final String description;
    private final int pos;
    private final Position[] array;

    private Position(int pos, String description) {
        this.pos = pos;
        this.description = description;
        this.array = new net.pcal.crimecats.Position[]{this};
    }

    public String getDescription() {
        return this.description;
    }

    public int getIndex() {
        return this.pos;
    }

    public Position[] asArray() {
        return this.array;
    }

    public Position offset(int by) {
        return Position.values()[(this.ordinal() + by) % Position.values().length];
    }

}
