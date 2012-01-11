package puzzle.model.position;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Down;
import puzzle.model.direction.Left;
import puzzle.model.direction.Right;
import puzzle.model.direction.Up;

public class Middle extends Position
{
    private static Middle instance = new Middle();

    private Middle()
    {
        // This is a singleton.
    }

    static Middle getInstance()
    {
        return Middle.instance;
    }

    @Override
    public Direction[] possibleMoves()
    {
        return new Direction[] { Down.getInstance(), Up.getInstance(),
                Left.getInstance(), Right.getInstance() };
    }
}
