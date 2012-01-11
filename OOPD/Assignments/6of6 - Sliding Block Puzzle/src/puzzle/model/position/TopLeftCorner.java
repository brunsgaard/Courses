package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Down;
import puzzle.model.direction.Right;

public class TopLeftCorner extends Position
{
    private static TopLeftCorner instance = new TopLeftCorner();

    private TopLeftCorner()
    {
        // This is a singleton.
    }

    static TopLeftCorner getInstance()
    {
        return TopLeftCorner.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Down.getInstance(), Right.getInstance() };
    }
}
