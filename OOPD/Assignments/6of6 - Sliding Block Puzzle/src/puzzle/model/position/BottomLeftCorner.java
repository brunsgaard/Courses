package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Right;
import puzzle.model.direction.Up;

public class BottomLeftCorner extends Position
{
    private static BottomLeftCorner instance = new BottomLeftCorner();

    private BottomLeftCorner()
    {
        // This is a singleton.
    }

    static BottomLeftCorner getInstance()
    {
        return BottomLeftCorner.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Up.getInstance(), Right.getInstance() };
    }
}
