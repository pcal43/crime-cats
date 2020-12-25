public class CrimeCats {

    public static void main(String[] args) {


    }

    class Solution {

    }

    interface Cat {
        String getName();
    }

    enum CatImpl implements Cat {

        SASSY("Sassy"),
        TOMCAT("Tom Cat");

        private final String name;

        CatImpl(String name) {
            this.name = name;
        };

        @Override
        public String getName() {
            return this.name;
        }
    }

    interface Clue {

        boolean matches(Solution solution);

    }

    class NextToClue implements Clue {

        private final cat1, cat2;

        @Override
        public boolean matches(Solution solution) {
            return false;
        }
    }

    class TwoAwayClue {

    }

    class ThreeAwayClue {

    }

}
