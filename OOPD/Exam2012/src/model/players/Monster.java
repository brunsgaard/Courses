package model.players;

import model.Point;

public abstract class Monster extends Player

{
    public Monster(Point position, int damage, int healthRegenerationRate)
    {
        super(position, damage, healthRegenerationRate);
    }

    @Override
    public int getDamageLevel()
    {
        return damage;
    }

    @Override
    public void takeDamage(int amount)
    {
        health -= amount;
    }
}
