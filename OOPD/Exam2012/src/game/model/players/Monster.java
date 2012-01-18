package game.model.players;

import game.controller.dungeon.Direction;
import game.model.Dungeon;
import game.model.Point;
import game.model.notification.PlayerDied;
import game.model.notification.PlayerHealthChanged;

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
        if (this.isDead())
            this.notifyObservers(new PlayerDied());
    }

    public void makeAutonomousMove()
    {
        Point newPosition = this.position.oneStep(this.towardsHero());
        if (!this.currentRoom.isInside(newPosition)) return;

    }
    
    private int manhattenDistance(Point from, Point to)
    {
        return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
    }

    public Direction towardsHero()
    {
        Point heroPos = Dungeon.getInstance().getHero().getPosition();
        int nd = this.manhattenDistance(heroPos,this.position.oneStep(Direction.NORTH));
        int sd = this.manhattenDistance(heroPos,this.position.oneStep(Direction.SOUTH));
        int ed = this.manhattenDistance(heroPos,this.position.oneStep(Direction.EAST));
        int wd = this.manhattenDistance(heroPos,this.position.oneStep(Direction.WEST));
        
        Direction shortestDirection = Direction.NORTH;
        int shortestDistance = nd;
        if (sd < shortestDistance) {
            shortestDistance = sd;
            shortestDirection = Direction.SOUTH;
        }
        if (ed < shortestDistance) {
            shortestDistance = ed;
            shortestDirection = Direction.EAST;
        }
        if (wd < shortestDistance) {
            shortestDistance = wd;
            shortestDirection = Direction.WEST;
        }
        return shortestDirection;
    }
}
