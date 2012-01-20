package game.model.players;

import game.controller.Observable;
import game.controller.Observer;
import game.controller.dungeon.Direction;
import game.controller.dungeon.TurnController;
import game.controller.notification.INotification;
import game.controller.notification.PlayerHealthChanged;
import game.controller.notification.TurnEnd;
import game.controller.notification.TurnStart;
import game.model.Point;
import game.model.Room;

public abstract class Player extends
        Observable<INotification, Observer<INotification>> implements
        Observer<INotification>
{
    protected Point position;
    protected int health;
    protected int unarmedDamage;
    protected int healthRegenerationRate;
    protected int hitPoints;
    protected Room room;

    public Player(Point position, int unarmedDamage, int healthRegenerationRate, int hitpoints)
    {
        this.position = position;
        this.hitPoints = hitpoints;
        this.health = this.hitPoints;
        this.unarmedDamage = unarmedDamage;
        this.healthRegenerationRate = healthRegenerationRate;
        TurnController.getInstance().addObserver(this);
    }

    public boolean isDead()
    {
        return this.health <= 0;
    }

    public void regenerate()
    {
        this.health = Math.min(this.hitPoints, this.health + healthRegenerationRate);
        this.notifyObservers(new PlayerHealthChanged(this.health));
    }

    public Point getPosition()
    {
        return this.position;
    }

    public boolean tryMove(Direction direction)
    {
        // Try move is a "Hero only" and , hence it
        // is moved to the hero class.
        return false;
    }

    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    public Room getCurrentRoom()
    {
        return room;
    }

    public void setCurrentRoom(Room currentRoom)
    {
        this.room = currentRoom;
    }

    public abstract int getDamageLevel();

    public abstract void takeDamage(int amount);

    public final void update(INotification change)
    {
        if (change instanceof TurnStart && !this.isDead())
            this.update((TurnStart) change);
        if (change instanceof TurnEnd)
            this.update((TurnEnd) change);
    }
    
    public void update(TurnStart change)
    {
        
    }

    public void update(TurnEnd change)
    {
        if (this.isDead())
        {
            this.room.removePlayer(this);
        } else
        {
            this.regenerate();
        }
    }

}
