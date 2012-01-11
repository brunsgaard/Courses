package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Left;
import puzzle.model.direction.Up;

public class BottomRightCorner extends Position
{
    private static BottomRightCorner instance = new BottomRightCorner();

    private BottomRightCorner()
    {
        // This is a singleton.
    }

    static BottomRightCorner getInstance()
    {
        return BottomRightCorner.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Up.getInstance(), Left.getInstance() };
    }
}
