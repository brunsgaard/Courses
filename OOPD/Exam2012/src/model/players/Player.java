package model.players;

import model.Point;

public abstract class Player
{
    protected Point position;
    protected int health;
    protected int damage;
    protected int healthRegenerationRate;

    public Player(Point position, int damage, int healthRegenerationRate)
    {
        this.position = position;
        this.health = 100;
        this.damage = damage;
        this.healthRegenerationRate = healthRegenerationRate;
    }

    public boolean isDead()
    {
        return this.health <= 0;
    }

    public void regenerate()
    {
        this.health = Math.min(100, this.health + this.healthRegenerationRate);
    }
    
    public abstract int getDamageLevel();
    
    public abstract void takeDamage(int amount);
    
    
}
