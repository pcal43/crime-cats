package net.pcal.crimecats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static net.pcal.crimecats.Preposition.*;

public interface ClueLUT {

    public Collection<Clue> getClues();

    static void main(String[] args) {
        final Collection<Clue> clues = NaiveClueLUT.create().getClues();
        for(Clue clue : clues) {
            System.out.println(clue);
        }
        System.out.println(clues.size()+" total clues.");
    }


    public static class NaiveClueLUT implements ClueLUT {

        private final Collection<Clue> clues;

        public static ClueLUT create() {
            final Collection<Clue> clues = new ArrayList<Clue>();
            final Collection<CatClue> indirectClues = new ArrayList<>();
            final Collection<CatClue> catClues = new ArrayList<>();
            catClues.addAll(Arrays.asList(Cat.values()));
            catClues.addAll(Arrays.asList(CatClue.CatFeature.values()));
            final Collection<PositionClue> posClues = new ArrayList<>();
            posClues.addAll(Arrays.asList(PositionClue.Position.values()));
            posClues.addAll(catClues);
            for (final CatClue cat : catClues) {
                for (final Preposition prep :  Preposition.values()) {
                    for (final PositionClue pos : posClues) {
                        clues.add(Clue.createRelative(cat, prep, pos));
                    }
                }
            }
            return new NaiveClueLUT(clues);
        }

        NaiveClueLUT(Collection<Clue> clues) {
            this.clues = clues;
        }

        @Override
        public Collection<Clue> getClues() {
            return clues;
        }
    }
}

