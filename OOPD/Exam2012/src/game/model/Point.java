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
        Point endPosition = new Point(-1, -1);

        switch (direction)
        {
        case NORTH:
            endPosition = new Point(this.x, this.y - 1);
            break;
        case SOUTH:
            endPosition = new Point(this.x,
                    this.y + 1);
            break;
        case WEST:
            endPosition = new Point(this.x - 1,
                    this.y);
            break;
        case EAST:
            endPosition = new Point(this.x + 1,
                    this.y);
            break;
        }
        return endPosition;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
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

    // public int hashCode()
    // {
    // return this.y * 300 + this.x;
    // }
    //
    // public boolean equals(Object obj) {
    // if(this == obj) {
    // return true;
    // }
    // if (!(obj instanceof Point)) {
    // return false;
    // }
    // Point point = (Point)obj;
    // return x == point.getX() && y == point.getY();
    //
    // }
}
