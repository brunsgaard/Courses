//msn378
package game.model.items;

import game.model.Point;

public class Potion extends Item

{
    private int points;
    
    public Potion(int points)
    {
        this.points = points;
    }

    public Potion(Point position, int points)
    {
        super(position);
        this.points = points;
    }

    public int getPoints()
    {
        return points;
    }
}