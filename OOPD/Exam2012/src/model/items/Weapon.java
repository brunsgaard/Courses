package model.items;

import model.Point;

public class Weapon extends Item

{
    private int damage;
    
    public Weapon(int damage)
    {
        this.damage = damage;
    }

    public Weapon(Point position, int damage)
    {
        super(position);
        this.damage = damage;
    }

    public int getDamage()
    {
        return damage;
    }

    public void setDamage(int damage)
    {
        this.damage = damage;
    }
}
