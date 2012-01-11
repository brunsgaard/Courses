package puzzle.model.direction;

import puzzle.model.BoardModel;

public abstract class Direction
{
    /**
     * @nonNegative if a new position could be calculated.
     * @negative if the new position could not be calculated, e.g. a left move
     *           from a left edge makes no sense.
     */
    public abstract int getNextPosition(BoardModel board);

    public abstract Direction opposite();
}