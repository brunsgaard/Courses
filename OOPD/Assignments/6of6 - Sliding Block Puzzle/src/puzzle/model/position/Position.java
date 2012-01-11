package puzzle.model.position;

import puzzle.model.direction.Direction;

public abstract class Position
{
    public abstract Direction[] possibleMoves();
}
