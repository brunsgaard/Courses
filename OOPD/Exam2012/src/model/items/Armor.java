package model.items;

import model.Point;

public class Armor extends Item

{
    private int resistence;
    
    public Armor(int damage)
    {
        this.resistence = damage;
    }

    public Armor(Point position, int damage)
    {
        super(position);
        this.resistence = damage;
    }

    public int getResistence()
    {
        return resistence;
    }

    public void setResistence(int resistence)
    {
        this.resistence = resistence;
    }
}
