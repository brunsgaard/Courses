package game.model;

import game.controller.dungeon.Direction;

public class Point
{
    private int x;
    private int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public Point oneStep(Direction direction)
    {
        switch (direction)
        {
        case NORTH:
            return new Point(this.x, this.y - 1);
        case SOUTH:
            return new Point(this.x, this.y + 1);
        case WEST:
            return new Point(this.x - 1, this.y);
        case EAST:
            return new Point(this.x + 1, this.y);
        default:
            return null;
        }
    }

    @Override
    public int hashCode()
    {
        return (this.y<<16)+this.x;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
