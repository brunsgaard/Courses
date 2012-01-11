package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Down;
import puzzle.model.direction.Right;
import puzzle.model.direction.Up;

public class LeftEdge extends Position
{
    private static LeftEdge instance = new LeftEdge();

    private LeftEdge()
    {
        // This is a singleton.
    }

    static LeftEdge getInstance()
    {
        return LeftEdge.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Down.getInstance(), Up.getInstance(),
                Right.getInstance() };
    }
}
