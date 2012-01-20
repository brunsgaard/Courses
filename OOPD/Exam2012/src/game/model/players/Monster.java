package game.model.players;

import game.controller.dungeon.Direction;
import game.controller.notification.PlayerDied;
import game.controller.notification.PlayerHealthChanged;
import game.controller.notification.PlayerMoved;
import game.controller.notification.TurnStart;
import game.model.Dungeon;
import game.model.Point;
import game.model.Room;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public abstract class Monster extends Player

{
    private Point doorOnRandomMove;

    public Monster(Point position, int unarmedDamage, int healthRegenerationRate)
    {
        super(position, unarmedDamage, healthRegenerationRate);
        this.doorOnRandomMove = null;
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
        System.out.println("Monsters Health: " + this.getHealth());
    }

    public void makeAutonomousMove()
    {
        Hero hero = Dungeon.getInstance().getHero();
        MovementPattern movementPattern = MovementPattern.DEFAULT;

        Direction moveDirection = null;
        Point endPosition = null;

        if (this.room.isInside(hero.getPosition()))
            movementPattern = MovementPattern.HERO_IN_ROOM;
        if (this.room.isInNeighborRoom(hero.getPosition()))
            movementPattern = MovementPattern.HERO_IN_NEXT_ROOM;

        switch (movementPattern)
        {
        case HERO_IN_ROOM:

            moveDirection = this.getDirection(hero.getPosition(),
                    MovementPattern.HERO_IN_ROOM);

            endPosition = this.position.oneStep(moveDirection);

            if (moveDirection == null)
                return;

            if (endPosition.equals(hero.getPosition()))
            {
                hero.takeDamage(this.getDamageLevel());

                if (hero.isDead())
                {
                    System.out.println("Hero Killed");
                    this.notifyObservers(new PlayerDied());
                }
                return;
            }

            if (this.manhattenDistance(hero.getPosition(), endPosition) < this
                    .manhattenDistance(hero.getPosition(), this.getPosition()))
            {
                this.position = endPosition;
                this.notifyObservers(new PlayerMoved(endPosition));
            }

            break;

        case HERO_IN_NEXT_ROOM:

            Point doorToHero = null;

            for (Point p : this.room.getDoors().keySet())
            {
                if (hero.getCurrentRoom().isInside(p))
                {
                    doorToHero = p;
                }
            }
            moveDirection = this.getDirection(doorToHero,
                    MovementPattern.HERO_IN_NEXT_ROOM);
            endPosition = this.position.oneStep(moveDirection);

            if (moveDirection == null)
                return;

            if (endPosition.equals(doorToHero))
            {
                this.autonomousChangeRoom(doorToHero);
            }

            this.position = endPosition;
            this.notifyObservers(new PlayerMoved(endPosition));

            break;

        case DEFAULT:

            if (this.doorOnRandomMove == null)
                this.setRandomDoorAsTarget();

            moveDirection = this.getDirection(doorOnRandomMove,
                    MovementPattern.DEFAULT);

            if (moveDirection == null)
                return;

            endPosition = this.position.oneStep(moveDirection);

            if (endPosition.equals(doorOnRandomMove))
            {
                this.autonomousChangeRoom(doorOnRandomMove);
            }

            this.position = endPosition;

            this.notifyObservers(new PlayerMoved(endPosition));

            break;
        }

    }

    private boolean isValidPosition(Point desiredEndPosition,
            MovementPattern movementPattern)
    {
        boolean returnValue = false;

        switch (movementPattern)
        {
        case HERO_IN_ROOM:
            returnValue = this.room.isInside(desiredEndPosition)
                    && (!Dungeon.getInstance().isDoor(desiredEndPosition)
                            || desiredEndPosition.equals(Dungeon.getInstance()
                                    .getHero().getPosition()) || this.room
                                .isInside(Dungeon.getInstance().getHero()
                                        .getPosition()))
                    && !Dungeon.getInstance().isMonsterOnPosition(
                            desiredEndPosition);
            break;

        case HERO_IN_NEXT_ROOM:
            returnValue = (this.room.checkForDoor(desiredEndPosition) || this.room
                    .isInside(desiredEndPosition))
                    && !Dungeon.getInstance().isMonsterOnPosition(
                            desiredEndPosition);
            break;

        case DEFAULT:
            returnValue = ((this.room.checkForDoor(desiredEndPosition) || this.room
                    .isInside(desiredEndPosition)))

            // TODO: what happens if two monsters meet in random walk an each
            // side of a door.. Testing needed..
                    && !Dungeon.getInstance().isMonsterOnPosition(
                            desiredEndPosition);
            break;
        }
        return returnValue;
    }

    public Direction getDirection(Point position,
            MovementPattern movementPattern)
    {
        Point pn = this.position.oneStep(Direction.NORTH);
        Point ps = this.position.oneStep(Direction.SOUTH);
        Point pe = this.position.oneStep(Direction.EAST);
        Point pw = this.position.oneStep(Direction.WEST);
        int nd = this.manhattenDistance(position, pn);
        int sd = this.manhattenDistance(position, ps);
        int ed = this.manhattenDistance(position, pe);
        int wd = this.manhattenDistance(position, pw);

        Direction shortestDirection = null;
        int shortestDistance = 301;
        if (nd < shortestDistance && this.isValidPosition(pn, movementPattern))
        {
            shortestDirection = Direction.NORTH;
            shortestDistance = nd;
        }
        if (sd < shortestDistance && this.isValidPosition(ps, movementPattern))
        {
            shortestDistance = sd;
            shortestDirection = Direction.SOUTH;
        }
        if (ed < shortestDistance && this.isValidPosition(pe, movementPattern))
        {
            shortestDistance = ed;
            shortestDirection = Direction.EAST;
        }
        if (wd < shortestDistance && this.isValidPosition(pw, movementPattern))
        {
            shortestDistance = wd;
            shortestDirection = Direction.WEST;
        }
        return shortestDirection;
    }

    private int manhattenDistance(Point from, Point to)
    {
        return Math.abs(from.getX() - to.getX())
                + Math.abs(from.getY() - to.getY());
    }

    private void autonomousChangeRoom(Point newPosition)
    {
        Room newRoom = this.room.getDoors().get(newPosition);
        ArrayList<Point> validDoors = new ArrayList<Point>();
        for (Point p : newRoom.getDoors().keySet())
        {
            if (newRoom.getDoors().size() == 1 || !p.equals(this.getPosition()))
                validDoors.add(new Point(p.getX(), p.getY()));
        }
        this.doorOnRandomMove = validDoors.get(new Random().nextInt(validDoors
                .size()));
        newRoom.addMonster(this);
        this.room.removePlayer(this);
        this.room = newRoom;
    }

    private void setRandomDoorAsTarget()
    {
        Set<Point> validDoors = this.room.getDoors().keySet();
        int randomTarget = new Random().nextInt(validDoors.size());
        int i = 0;
        for (Point p : validDoors)
        {
            if (i++ == randomTarget)
            {
                this.doorOnRandomMove = p;
                break;
            }
        }
    }

    public void update(TurnStart change)
    {
        super.update(change);
        this.makeAutonomousMove();
    }

}
