package puzzle.model.direction;

import puzzle.model.BoardModel;

public class Left extends Direction
{
    private static Left instance = new Left();

    private Left()
    {
        // This is a singleton.
    }

    public static Left getInstance()
    {
        return Left.instance;
    }

    @Override
    public String toString()
    {
        return "left";
    }

    @Override
    public int getNextPosition(BoardModel board)
    {
        int newPosition = board.getPositionOfEmptySlot();
        if (newPosition % board.getNumberOfSlotsInARow() == 0)
            return -1;
        return --newPosition;
    }

    @Override
    public Direction opposite()
    {
        return Right.getInstance();
    }
}