package net.pcal.crimecats;

import java.util.ArrayList;
import java.util.List;

public class RelativeClue implements Clue {

    public static void main(String[] args) {
        final List<RelativeClue> clues = createRelativeClues();
        for(Clue clue : clues) {
            System.out.println(clue);
        }
        System.out.println(clues.size()+" total clues.");
    }


    private SubjectClue subject;
    private Preposition prep;
    private TargetClue target;


    @Override
    public boolean matches(final Solution sol) {
        for (final Position pos : subject.getSubjectPositions(sol)) {
            for (final Position relativePos : prep.getPossibilities(pos)) {
                if (this.target.matches(sol, relativePos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private interface SubjectClue {
        Position[] getSubjectPositions(Solution sol);

        Cat[] getPossibleCats();

        String getDescription();
    }

    private interface TargetClue {
        boolean matches(Solution solution, Position p);

        Cat[] getPossibleCats();

        String getDescription();
    }

    private static class CatClue implements SubjectClue, TargetClue {

        private final Cat cat;

        private CatClue(Cat cat) {
            this.cat = cat;
        }

        @Override
        public Position[] getSubjectPositions(Solution sol) {
            return sol.getPositionOf(this.cat).asArray();
        }

        @Override
        public boolean matches(Solution solution, Position p) {
            return false;
        }

        @Override
        public Cat[] getPossibleCats() {
            return this.cat.asArray();
        }

        @Override
        public String getDescription() {
            return this.cat.getDescription();
        }
    }

    private static class CatFeatureClue implements SubjectClue, TargetClue {

        private final CatFeature catFeature;

        private CatFeatureClue(CatFeature catFeature) {
            this.catFeature = catFeature;
        }

        @Override
        public Position[] getSubjectPositions(Solution sol) {
            final Cat[] cats = this.catFeature.getPossibleCats();
            final Position[] out = new Position[cats.length];
            for (int i = 0; i < out.length; i++) {
                out[i] = sol.getPositionOf(cats[i]);
            }
            return out;
        }

        @Override
        public boolean matches(Solution sol, Position pos) {
            final Cat cat = sol.getCatAt(pos);
            for (Cat fc : this.catFeature.getPossibleCats()) {
                if (fc == cat) return true;
            }
            return false;
        }

        @Override
        public Cat[] getPossibleCats() {
            return this.catFeature.getPossibleCats();
        }

        @Override
        public String getDescription() {
            return this.catFeature.getDescription();
        }
    }


    private static class PositionClue implements TargetClue {

        private final Position position;

        private PositionClue(Position position) {
            this.position = position;
        }

        @Override
        public boolean matches(Solution ignored, Position position) {
            return position == this.position;
        }

        @Override
        public String getDescription() {
            return this.position.getDescription();
        }

        @Override
        public Cat[] getPossibleCats() {
            return new Cat[0];
        }
    }

    private static class PositionFeatureClue implements TargetClue {

        private final PositionFeature positionFeature;

        private PositionFeatureClue(PositionFeature positionFeature) {
            this.positionFeature = positionFeature;
        }

        @Override
        public boolean matches(Solution ignored, Position position) {
            for (Position p : this.positionFeature.getPossiblePositions()) {
                if (p == position) return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return this.positionFeature.getDescription();
        }

        @Override
        public Cat[] getPossibleCats() {
            return new Cat[0];
        }
    }

    private RelativeClue(SubjectClue subject, Preposition prep, TargetClue target) {
        this.subject = subject;
        this.prep = prep;
        this.target = target;
    }


    private static List<RelativeClue> createRelativeClues() {
        final List<TargetClue> targetClues = new ArrayList<>();
        final List<SubjectClue> subjectClues = new ArrayList<>();

        for (final Cat cat : Cat.values()) {
            CatClue clue = new CatClue(cat);
            subjectClues.add(clue);
            targetClues.add(clue);
        }
        for (final CatFeature cat : CatFeature.values()) {
            CatFeatureClue clue = new CatFeatureClue(cat);
            subjectClues.add(clue);
            targetClues.add(clue);
        }
        for (final Position pos : Position.values()) {
            targetClues.add(new PositionClue(pos));
        }
        for (final PositionFeature pf : PositionFeature.values()) {
            targetClues.add(new PositionFeatureClue(pf));
        }

        final List<RelativeClue> out = new ArrayList<RelativeClue>();
        for (final SubjectClue sub : subjectClues) {
            for (final Preposition prep : Preposition.PrepositionImpl.values()) {
                for (final TargetClue tar : targetClues) {
                    if (isIntersection(sub.getPossibleCats(), tar.getPossibleCats())) {
                        continue;
                    }
                    out.add(new RelativeClue(sub, prep, tar));
                }
            }
        }
        return out;
    }

    private static <T extends Object> boolean isIntersection(T[] o1, T[] o2) {
        for (int i = 0; i < o1.length; i++) {
            for (int j = 0; j < o2.length; j++) {
                if (o1[i] == o2[j]) return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.subject.getDescription() + " " + this.prep.getDescription() + " " + this.target.getDescription();
    }
}
