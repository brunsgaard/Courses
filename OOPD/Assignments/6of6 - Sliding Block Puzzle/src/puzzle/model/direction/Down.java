package puzzle.model.direction;

import puzzle.model.BoardModel;

public class Down extends Direction
{
    private static Down instance = new Down();

    private Down()
    {
        // This is a singleton.
    }

    public static Down getInstance()
    {
        return Down.instance;
    }

    @Override
    public String toString()
    {
        return "down";
    }

    @Override
    public int getNextPosition(BoardModel boardModel)
    {
        /*
         * int newPosition = boardModel.getPositionOfEmptySlot() +
         * boardModel.getNumberOfSlotsInARow(); if (newPosition >
         * boardModel.getNumberOfSlots() - 1) return -1;
         * 
         * return newPosition;
         */
        // TODO: by student
        return -1;
    }

    @Override
    public Direction opposite()
    {
        return Up.getInstance();
    }
}