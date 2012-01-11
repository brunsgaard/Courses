package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Down;
import puzzle.model.direction.Left;
import puzzle.model.direction.Right;

public class TopEdge extends Position
{
    private static TopEdge instance = new TopEdge();

    private TopEdge()
    {
        // This is a singleton.
    }

    static TopEdge getInstance()
    {
        return TopEdge.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Down.getInstance(), Left.getInstance(),
                Right.getInstance() };
    }
}
