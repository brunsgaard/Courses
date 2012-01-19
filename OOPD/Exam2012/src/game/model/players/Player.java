package game.model.players;

import game.controller.Observable;
import game.controller.Observer;
import game.controller.dungeon.Direction;
import game.controller.dungeon.TurnController;
import game.model.Point;
import game.model.Room;
import game.model.items.Item;
import game.model.notification.ChangeRoom;
import game.model.notification.INotification;
import game.model.notification.PlayerDied;
import game.model.notification.PlayerHealthChanged;
import game.model.notification.PlayerMoved;
import game.model.notification.TurnEnd;
import game.model.notification.TurnStart;

public abstract class Player extends
        Observable<INotification, Observer<INotification>> implements
        Observer<INotification>
{
    protected Point position;
    protected int health;
    protected int unarmedDamage;
    protected int healthRegenerationRate;
    protected Room currentRoom;

    public Player(Point position, int unarmedDamage, int healthRegenerationRate)
    {
        this.position = position;
        this.health = 100;
        this.unarmedDamage = unarmedDamage;
        this.healthRegenerationRate = healthRegenerationRate;
        TurnController.getInstance().addObserver(this);
    }

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
            this.currentRoom.removePlayer(this);
        } else {
            this.regenerate();            
        }
    }

    public boolean isDead()
    {
        return this.health <= 0;
    }

    public void regenerate()
    {
        this.health = Math.min(100, this.health + healthRegenerationRate);
        this.notifyObservers(new PlayerHealthChanged(this.health));
    }

    public abstract int getDamageLevel();

    public abstract void takeDamage(int amount);

    public Point getPosition()
    {
        return this.position;
    }

    public boolean tryMove(Direction direction)
    {

        // the end position of the move
        Point newPosition = position.oneStep(direction);

        // check for door else wall
        if (this.currentRoom.checkForDoor(newPosition))
        {
            this.currentRoom.removePlayer(this);
            this.currentRoom = currentRoom.getDoors().get(newPosition);
            this.notifyObservers(new ChangeRoom(this.currentRoom));
        } else if (!this.currentRoom.isInside(newPosition))
        {
            return false;
        }

        // check for monster at end position
        Monster monster = this.currentRoom
                .getMonsterFromNewPosition(newPosition);

        if (monster != null)
        {
            monster.takeDamage(this.getDamageLevel());
            // TODO Debug info
            System.out.println("Monsters Health: " + monster.getHealth());

            return true;
        }

        // check for items
        // TODO Do something with item
        Item item = this.currentRoom.loot(newPosition);

        if (item != null)
        {
            System.out.println("uhhh.. it looks like an item");
            currentRoom.removeItem(item);
        }

        // set position and notify observers..
        this.position = newPosition;
        this.notifyObservers(new PlayerMoved(this.position));

        return true;
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
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom)
    {
        this.currentRoom = currentRoom;
    }

}
