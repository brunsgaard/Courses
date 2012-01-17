package model.players;

import model.Point;

public abstract class Player
{
    protected Point position;
    protected int health;
    protected static int unarmedDamage;
    protected static int healthRegenerationRate;

    public Player(Point position)
    {
        this.position = position;
        this.health = 100;
    }

    public boolean isDead()
    {
        return this.health <= 0;
    }

    public void regenerate()
    {
        this.health = Math.min(100, this.health + healthRegenerationRate);
    }
    
    public abstract int getDamageLevel();
    
    public abstract void takeDamage(int amount);
    
    
}
