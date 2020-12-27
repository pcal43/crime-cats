package net.pcal.crimecats;

import static net.pcal.crimecats.Position.*;

public enum PositionFeature {

    CATNIP("catnip", SHOES, FLOWER),
    PAW("a paw print", COFFEE, YARN),
    CLAW("claw marks", SHOES, BIRD),
    SOCKS("a sock", FISH, FLOWER),
    MOUSE("a mouse", YARN, BIRD),
    BELL("a bell ball", COFFEE, FISH);

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
