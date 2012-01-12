package puzzle.model.direction;

import puzzle.model.BoardModel;

public class Up extends Direction
{
    private static Up instance = new Up();

    private Up()
    {
        // This is a singleton.
    }

    public static Up getInstance()
    {
        return Up.instance;
    }

    @Override
    public String toString()
    {
        return "up";
    }

    @Override
    public int getNextPosition(BoardModel boardModel)
    {
        
        int newPosition = boardModel.getPositionOfEmptySlot()
                - boardModel.getNumberOfSlotsInARow();
        if (newPosition < 0)
            return -1;
        return newPosition;
        
    }

    @Override
    public Direction opposite()
    {
        return Down.getInstance();
    }
}