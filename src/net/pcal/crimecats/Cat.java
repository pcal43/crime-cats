package net.pcal.crimecats;

public enum Cat {

    SASSY("Sassy"),
    DUCHESS("Duchess"),
    GINGER("Ginger"),
    PIPSQUEAK("Pip Squeak"),
    MITTENS("Mr. Mittens"),
    TOMCAT("Tom Cat");

    private final String name;
    private final Cat[] array;

    Cat(String name) {
        this.name = name;
        this.array = new Cat[] { this };
    }
    public String getDescription() {
        return this.name;
    }

    public Cat[] asArray() { return this.array; }


}
