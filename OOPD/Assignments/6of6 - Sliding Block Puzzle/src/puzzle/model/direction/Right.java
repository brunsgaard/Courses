package puzzle.model.direction;

import puzzle.model.BoardModel;

public class Right extends Direction
{
    private static Right instance = new Right();

    private Right()
    {
        // This is a singleton.
    }

    public static Right getInstance()
    {
        return Right.instance;
    }

    @Override
    public String toString()
    {
        return "right";
    }

    @Override
    public int getNextPosition(BoardModel board)
    {
        
        int newPosition = board.getPositionOfEmptySlot() + 1;
        if (newPosition % board.getNumberOfSlotsInARow() == 0)
            return -1;
        return newPosition;
    }

    @Override
    public Direction opposite()
    {
        return Left.getInstance();
    }
}