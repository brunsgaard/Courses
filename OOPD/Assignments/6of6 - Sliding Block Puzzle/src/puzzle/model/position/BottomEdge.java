package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Left;
import puzzle.model.direction.Right;
import puzzle.model.direction.Up;

public class BottomEdge extends Position
{
    private static BottomEdge instance = new BottomEdge();

    private BottomEdge()
    {
        // This is a singleton.
    }

    static BottomEdge getInstance()
    {
        return BottomEdge.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Up.getInstance(), Left.getInstance(),
                Right.getInstance() };
    }
}
