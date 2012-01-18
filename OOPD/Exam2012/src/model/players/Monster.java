package model.players;

import model.Point;
import model.notification.PlayerDied;
import model.notification.PlayerHealthChanged;

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
        this.notifyObservers(new PlayerHealthChanged(this.health));
        if (this.isDead()) this.notifyObservers(new PlayerDied());
    }
}
