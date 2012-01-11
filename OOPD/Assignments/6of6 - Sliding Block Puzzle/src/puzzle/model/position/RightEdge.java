package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Down;
import puzzle.model.direction.Left;
import puzzle.model.direction.Up;

public class RightEdge extends Position
{
    private static RightEdge instance = new RightEdge();

    private RightEdge()
    {
        // This is a singleton.
    }

    static RightEdge getInstance()
    {
        return RightEdge.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Down.getInstance(), Up.getInstance(),
                Left.getInstance() };
    }
}
