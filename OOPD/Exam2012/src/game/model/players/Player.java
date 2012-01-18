package game.model.players;

import game.controller.Observable;
import game.controller.Observer;
import game.controller.dungeon.Direction;
import game.model.Point;
import game.model.Room;
import game.model.items.Item;
import game.model.notification.INotification;
import game.model.notification.PlayerHealthChanged;
import game.model.notification.PlayerMoved;

public abstract class Player extends
        Observable<INotification, Observer<INotification>>
{
    protected Point position;
    protected int health;
    protected static int unarmedDamage;
    protected static int healthRegenerationRate;
    protected Room currentRoom;

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
        if (this.currentRoom.getDoors().containsKey(newPosition))
        {
            this.currentRoom.removePlayer(this);
            this.currentRoom = currentRoom.getDoors().get(newPosition);
            this.currentRoom.setPlayer(this);

            // TODO NOTIFY changeRoom
        } else if (!this.currentRoom.isInside(newPosition))
        {
            return false;
        }
        

        // check for monster at end position
        // i
        Monster monster = this.currentRoom.getMonsterIfPresent(newPosition);

        if (monster != null)
        {
            monster.takeDamage(10);
            // TODO Debug info
            System.out.println("Monsters Health: " + monster.getHealth());

            if (monster.isDead())
            {
                currentRoom.removeMonster(monster);
                // DOes it have to be there
                // this.notifyObservers(new PlayerDied());
            }
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
        ;

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
