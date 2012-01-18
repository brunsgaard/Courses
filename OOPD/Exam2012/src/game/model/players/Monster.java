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
        Point newPosition = this.towardsHero();
        
        if (!this.currentRoom.isInside(newPosition)) return;

    }

    public Point towardsHero()
    {

        Point heroPos = Dungeon.getInstance().getHero().getPosition();

        Point north = this.position.oneStep(Direction.NORTH);
        Point south = this.position.oneStep(Direction.SOUTH);
        Point east = this.position.oneStep(Direction.EAST);
        Point west = this.position.oneStep(Direction.WEST);

        int northDis = Math.abs(north.getX() - heroPos.getX())
                + Math.abs(Math.abs(north.getY() - heroPos.getY()));

        int southDis = Math.abs(south.getX() - heroPos.getX())
                + Math.abs(Math.abs(south.getY() - heroPos.getY()));

        int eastDis = Math.abs(east.getX() - heroPos.getX())
                + Math.abs(Math.abs(east.getY() - heroPos.getY()));

        int westDis = Math.abs(west.getX() - heroPos.getX())
                + Math.abs(Math.abs(west.getY() - heroPos.getY()));

        int minDis = northDis;
        Point towardsHero = north;

        if (minDis > southDis)
        {
            minDis = southDis;
            towardsHero = south;
        }
        if (minDis > eastDis)
        {
            minDis = eastDis;
            towardsHero = east;
        }
        if (minDis > westDis)
        {
            towardsHero = south;
        }

        return towardsHero;

    }
}
