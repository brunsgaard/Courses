package model.players;

import controller.Direction;
import diku.oopd.Observable;
import diku.oopd.Observer;
import model.Point;
import model.notification.INotification;
import model.notification.PlayerHealthChanged;

public abstract class Player extends Observable<INotification, Observer<INotification>>
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
        this.notifyObservers(new PlayerHealthChanged(this.health));
    }
    
    public abstract int getDamageLevel();
    
    public abstract void takeDamage(int amount);
    
    public Point getPosition()
    {
        return this.position;
    }
    // TODO: directions
    public void tryMove(Direction direction){
       switch (direction){
       case UP:
           this.position = new Point(this.position.getX(), this.position.getY()+1);
           break;
       case DOWN:
           this.position = new Point(this.position.getX(), this.position.getY()-1);
           break;
       case LEFT:
           this.position = new Point(this.position.getX()-1, this.position.getY());
           break;
       case RIGHT:
           this.position = new Point(this.position.getX()+1, this.position.getY());
           break;
       }
    }
    
    
}
