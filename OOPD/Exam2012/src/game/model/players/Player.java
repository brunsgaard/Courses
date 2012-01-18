package game.model.players;

import game.controller.Observable;
import game.controller.Observer;
import game.controller.dungeon.Direction;
import game.model.Point;
import game.model.Room;
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
        // TODO: remove hard coded room
        this.currentRoom = new Room(new Point(0, 0), new Point(4, 5));
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

        Point newPosition = position.oneStep(direction);

        // TODO DEBUG: Stuff
        System.out.println("start:" + this.position.getX() + " "
                + position.getY());
        System.out.println("end:" + newPosition.getX() + " "
                + newPosition.getY());

        if (!currentRoom.isInside(newPosition))
        {
            // TODO DEBUG: Stuff
            System.out.println("end is outside");
            return false;
        }

        this.position = newPosition;

        this.notifyObservers(new PlayerMoved(this.position));
        return true;
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
