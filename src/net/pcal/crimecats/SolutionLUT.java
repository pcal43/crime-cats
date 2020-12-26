package net.pcal.crimecats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface SolutionLUT {

    Solution getSolution(int i);

    int getCount();

    public static void main(String[] args) {
        final SolutionLUT lut = NaiveSolutionLUT.create();
        for (int i=0; i<lut.getCount();i++) {
            System.out.println(lut.getSolution(i));
        }
        System.out.println(lut.getCount()+" total solutions");
    }


    static class NaiveSolutionLUT implements SolutionLUT {

        private final List<Solution> lut;

        private NaiveSolutionLUT(List<Solution> lut) {
            this.lut = lut;
        }

        @Override
        public Solution getSolution(int i) {
            return this.lut.get(i);
        }

        @Override
        public int getCount() {
            return this.lut.size();
        }

        static SolutionLUT create() {
            final List<Cat[]> solutions = new ArrayList<>();
            int size = Cat.values().length;
            final List<Solution> lut = new ArrayList<>();
            generate(Arrays.copyOf(Cat.values(), size), size, size, lut);
            return new NaiveSolutionLUT(lut);
        }


        private static void swap(Cat[] cats, int x, int y) {
            Cat temp = cats[x];
            cats[x] = cats[y];
            cats[y] = temp;
        }

        private static void generate(Cat[] cats, int size, int n, Collection<Solution> lut) {
            if (size == 1) {
                lut.add(Solution.create(Arrays.copyOf(cats, cats.length)));
                return;
            }
            for (int i = 0; i < size; i++) {
                generate(cats, size - 1, n, lut);
                // if size is odd, swap 0th i.e (first) and
                // (size-1)th i.e (last) element
                if (size % 2 == 1) {
                    swap(cats, 0, size - 1);
                } else {
                    // If size is even, swap ith and
                    // (size-1)th i.e (last) element
                    swap(cats, i, size -1 );
                }
            }
        }

    }
}
