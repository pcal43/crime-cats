package net.pcal.crimecats;


public interface PositionClue {

    String getDescription();

    Position[] getPossiblePositions(Solution solution);

}