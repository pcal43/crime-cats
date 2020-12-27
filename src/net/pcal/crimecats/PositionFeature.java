package net.pcal.crimecats;

import static net.pcal.crimecats.Position.*;

public enum PositionFeature {

    CATNIP("catnip", ONE,TWO);

    private final Position[] positions;
    private final String description;

    private PositionFeature(String description, Position... positions) {
        this.description = description;
        this.positions = positions;
    }

    public String getDescription() {
        return this.description;
    }

    public Position[] getPossiblePositions() {
        return this.positions;
    }

}
