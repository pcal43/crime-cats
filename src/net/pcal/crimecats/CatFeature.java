package net.pcal.crimecats;

import static net.pcal.crimecats.Cat.*;

public enum CatFeature {

    BLUE_EYES("a cat with blue eyes", TOMCAT, GINGER),
    WHITE_PAWS("a cat with white paws", MITTENS, SASSY),
    BELL("a cat wearing a bell", MITTENS, PIPSQUEAK),
    LONG_HAIR("a cat with long hair", SASSY, DUCHESS),
    STRIPES("a cat with stripes", GINGER, PIPSQUEAK),
    BOW("a cat with a bow", TOMCAT, DUCHESS);

    private final Cat[] cats;
    private final String description;

    private CatFeature(String description, Cat... cats) {
        this.description = description;
        this.cats = cats;
    }

    public String getDescription() {
        return this.description;
    }

    public Cat[] getPossibleCats() {
        return this.cats;
    }

}
