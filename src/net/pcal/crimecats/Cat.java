package net.pcal.crimecats;

public enum Cat implements CatClue {

    SASSY("Sassy"),
    DUCHESS("Duchess"),
    GINGER("Ginger"),
    PIPSQUEAK("Pip Squeak"),
    MITTENS("Mr. Mittens"),
    TOMCAT("Tom Cat");

    private final String name;
    private final Cat[] cats;

    Cat(String name) {
        this.name = name;
        this.cats = new Cat[] {this};
    }

    @Override
    public String getDescription() {
        return this.name;
    }

    @Override
    public Cat[] getPossibleCats() {
        return this.cats;
    }
}
