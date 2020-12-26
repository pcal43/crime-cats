package net.pcal.crimecats;

public final class Solution {

    static Solution create(Cat[] positions) {
        return new Solution(positions);
    }

    private final Cat[] positions;

    private Solution(Cat[] positions) {
        this.positions = positions;
    }

    Cat getCatAt(final PositionClue.Position p) {
        return positions[p.getIndex()];
    }

    public String toString() {
        final StringBuilder out = new StringBuilder();
        for(Cat cat : this.positions) {
            out.append(cat.name()+",");
        }
        return out.toString();

    }
}
