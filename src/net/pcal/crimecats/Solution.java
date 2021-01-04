package net.pcal.crimecats;

import java.io.PrintWriter;

public final class Solution {

    static Solution create(Cat[] positions) {
        return new Solution(positions);
    }

    private final Cat[] positions;

    private Solution(Cat[] positions) {
        this.positions = positions;
    }

    Cat getCatAt(final Position p) {
        return positions[p.getIndex()];
    }

    Position getPositionOf(final Cat cat) {
        //FIXME stupid
        for (int i = 0; i < this.positions.length; i++) {
            if (positions[i] == cat) return Position.values()[i];
        }
        throw new IllegalStateException();
    }

    public String toString() {
        final StringBuilder out = new StringBuilder();
        for (Cat cat : this.positions) {
            out.append(cat.name() + ",");
        }
        return out.toString();

    }

    public void print(PrintWriter pw) {
        for (int i = 0; i < positions.length; i++) {
            pw.println((i + 1) + ". " + positions[i].getDescription() + " (" + Position.values()[i].getDescription() + ")");
        }
    }
}
