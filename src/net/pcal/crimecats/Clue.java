package net.pcal.crimecats;

import net.pcal.crimecats.PositionClue.Position;

public interface Clue {

    boolean matches(Solution solution);

    static Clue createRelative(CatClue cat, Preposition prep, PositionClue pos) {
        return new ClueImpl(cat,prep,pos);
    }

    static class ClueImpl implements Clue {

        private CatClue catClue;
        private Preposition prep;
        private PositionClue posClue;

        ClueImpl(CatClue cat, Preposition prep, PositionClue pos) {
            this.catClue = cat;
            this.prep = prep;
            this.posClue = pos;
        }

        @Override
        public boolean matches(final Solution solution) {
            for (final Position pos : posClue.getPossiblePositions(solution)) {
                for(final Position relativePos : prep.getPossibilities(pos)) {
                    for (final Cat cat : catClue.getPossibleCats()) {
                        if (solution.getCatAt(relativePos) == cat) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public String toString() {
            return this.catClue.getDescription()+" "+this.prep.getDescription()+" "+this.posClue.getDescription();
        }
    }

}
