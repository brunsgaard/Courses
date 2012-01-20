package game.model.items;

import game.model.Point;

public abstract class Item
{
    protected Point position;
    
    protected Item()
    {
        this.position = null;
    }

    protected Item(Point position)
    {
        this.position = position;
    }

    public Point getPosition()
    {
        return position;
    }

}
