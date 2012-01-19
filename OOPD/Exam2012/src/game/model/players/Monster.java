package game.model.players;

import game.controller.dungeon.Direction;
import game.controller.notification.PlayerDied;
import game.controller.notification.PlayerHealthChanged;
import game.controller.notification.TurnStart;
import game.model.Dungeon;
import game.model.Point;

public abstract class Monster extends Player

{
    public Monster(Point position, int unarmedDamage, int healthRegenerationRate)
    {
        super(position, unarmedDamage, healthRegenerationRate);
    }

    public void update(TurnStart change)
    {
        super.update(change);
        this.makeAutonomousMove();
    }

    @Override
    public int getDamageLevel()
    {
        return unarmedDamage;
    }

    @Override
    public void takeDamage(int amount)
    {
        this.health -= amount;
        this.notifyObservers(new PlayerHealthChanged(this.health));
        if (this.isDead())
            this.notifyObservers(new PlayerDied());
    }

    public void makeAutonomousMove()
    {
        Hero hero = Dungeon.getInstance().getHero();
        Direction direction = this.towardsHero();
        if (direction == null)
            return;
        Point newPosition = this.position.oneStep(direction);

        if (newPosition.equals(hero.getPosition()))
        {
            hero.takeDamage(this.getDamageLevel());

            if (hero.isDead())
            {
                // Action on dead Hero
                System.out.println("Hero Killed");
                this.notifyObservers(new PlayerDied());
            }

            return;
        }

        if (this.manhattenDistance(hero.getPosition(), newPosition) < this
                .manhattenDistance(hero.getPosition(), this.getPosition()))
        {
            this.position = newPosition;
            this.manhattenDistance(hero.getPosition(), newPosition);
        }
    }

    private boolean isValidPosition(Point newPosition)
    {

        // Has to be in the room
        return this.currentRoom.isInside(newPosition)
                // Cannot move to a door unless hero is present on the door or
                // in the room.
                && (!Dungeon.getInstance().isDoor(newPosition)
                        || newPosition.equals(Dungeon.getInstance().getHero()
                                .getPosition()) || this.currentRoom
                            .isInside(Dungeon.getInstance().getHero()
                                    .getPosition()))
                // Can not move to a position with a monster
                && !this.currentRoom.isMonsterOnPosition(newPosition);
    }

    private int manhattenDistance(Point from, Point to)
    {
        return Math.abs(from.getX() - to.getX())
                + Math.abs(from.getY() - to.getY());
    }

    public Direction towardsHero()
    {
        Point heroPos = Dungeon.getInstance().getHero().getPosition();
        Point pn = this.position.oneStep(Direction.NORTH);
        Point ps = this.position.oneStep(Direction.SOUTH);
        Point pe = this.position.oneStep(Direction.EAST);
        Point pw = this.position.oneStep(Direction.WEST);
        int nd = this.manhattenDistance(heroPos, pn);
        int sd = this.manhattenDistance(heroPos, ps);
        int ed = this.manhattenDistance(heroPos, pe);
        int wd = this.manhattenDistance(heroPos, pw);

        Direction shortestDirection = null;
        int shortestDistance = 301;
        if (nd < shortestDistance && this.isValidPosition(pn))
        {
            shortestDirection = Direction.NORTH;
            shortestDistance = nd;
        }
        if (sd < shortestDistance && this.isValidPosition(ps))
        {
            shortestDistance = sd;
            shortestDirection = Direction.SOUTH;
        }
        if (ed < shortestDistance && this.isValidPosition(pe))
        {
            shortestDistance = ed;
            shortestDirection = Direction.EAST;
        }
        if (wd < shortestDistance && this.isValidPosition(pw))
        {
            shortestDistance = wd;
            shortestDirection = Direction.WEST;
        }
        return shortestDirection;
    }
}
