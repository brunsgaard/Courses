package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Down;
import puzzle.model.direction.Left;

public class TopRightCorner extends Position
{
    private static TopRightCorner instance = new TopRightCorner();

    private TopRightCorner()
    {
        // This is a singleton.
    }

    static TopRightCorner getInstance()
    {
        return TopRightCorner.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Down.getInstance(), Left.getInstance() };
    }
}
