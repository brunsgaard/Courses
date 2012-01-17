package model.players;

import model.Point;

public abstract class Monster extends Player

{
    public Monster(Point position)
    {
        super(position);
    }

    @Override
    public int getDamageLevel()
    {
        return unarmedDamage;
    }

    @Override
    public void takeDamage(int amount)
    {
        health -= amount;
    }
}
