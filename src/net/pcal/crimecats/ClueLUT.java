package net.pcal.crimecats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static net.pcal.crimecats.Preposition.*;

public interface ClueLUT {

    public Iterator<Clue> getClues();


    public static class NaiveClueLUT implements ClueLUT {

        static ClueLUT create() {
            final Collection<CatClue> indirectClues = new ArrayList<>();
            for(Cat cat : Cat.values()) {

            }

            final Collection<Clue> clues = new ArrayList<Clue>();
            for(Cat cat : Cat.values()) {
                for(final Preposition p : new Preposition[] { AT, NOT_AT} ) {
                    for(final PositionClue.Position p : PositionClue.Position.values()) {
                        clues.add(new Clue())

                }

            }



        }

        @Override
        public Iterator<Clue> getClues() {
            return null;
        }
    }

}
