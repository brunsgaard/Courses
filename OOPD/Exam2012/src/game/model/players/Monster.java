package game.model.players;

import game.controller.dungeon.Direction;
import game.controller.notification.ChangeRoom;
import game.controller.notification.PlayerDied;
import game.controller.notification.PlayerHealthChanged;
import game.controller.notification.PlayerMoved;
import game.controller.notification.TurnStart;
import game.model.Dungeon;
import game.model.Point;

import java.util.Random;
import java.util.Set;

public abstract class Monster extends Player

{
    Point doorOnRandomMove;

    public Monster(Point position, int unarmedDamage, int healthRegenerationRate)
    {
        super(position, unarmedDamage, healthRegenerationRate);
        this.doorOnRandomMove = null;
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
        MovementPattern movementPattern = MovementPattern.DEFAULT;

        Direction moveDirection = null;
        Point endPoint = null;

        if (this.currentRoom.isInside(hero.getPosition()))
            movementPattern = MovementPattern.HERO_IN_ROOM;
        if (this.currentRoom.isInNeighborRoom(hero.getPosition()))
            movementPattern = MovementPattern.HERO_IN_NEXT_ROOM;

        switch (movementPattern)
        {
        case HERO_IN_ROOM:
            moveDirection = this.getDirection(hero.getPosition(),
                    MovementPattern.HERO_IN_ROOM);

            // determine new position
            endPoint = this.position.oneStep(moveDirection);

            // If all move are invalid
            if (moveDirection == null)
                return;

            if (endPoint.equals(hero.getPosition()))
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

            if (this.manhattenDistance(hero.getPosition(), endPoint) < this
                    .manhattenDistance(hero.getPosition(), this.getPosition()))
            {
                this.position = endPoint;
                this.notifyObservers(new PlayerMoved(endPoint));
            }

            break;
        case HERO_IN_NEXT_ROOM:

            Point doorToHero = null;

            for (Point p : this.currentRoom.getDoors().keySet())
            {
                if (hero.getCurrentRoom().isInside(p))
                {
                    doorToHero = p;
                }
            }
            moveDirection = this.getDirection(doorToHero,
                    MovementPattern.HERO_IN_NEXT_ROOM);
            endPoint = this.position.oneStep(moveDirection);

            if (moveDirection == null)
                return;

            System.out.println("newPosition.equals(hero.getPosition()");


            if (endPoint.equals(doorToHero))
            {

                this.currentRoom.removePlayer(this);
                this.currentRoom = currentRoom.getDoors().get(endPoint);
                this.currentRoom.addMonster(this);
                this.setDoorOnRandomMove();
            }

            this.position = endPoint;
            this.notifyObservers(new PlayerMoved(endPoint));

            break;

        case DEFAULT:
            System.out.println("DEFAULT MOVE");

            // moveDirection = this.getDirectionTowardsPoint(doorOnRandomMove,
            // MovementPattern.DEFAULT);
            //
            // if (moveDirection == null)
            // return;
            //
            // newPosition = this.position.oneStep(moveDirection);
            //
            // System.out.println("current pos x= " + this.position.getX()
            // + "current pos y= " + this.position.getY());
            //
            // System.out.println("newPosition pos x= " + newPosition.getX()
            // + "newPosition pos y= " + newPosition.getY());
            //
            // System.out.println("Goal pos x= " + doorOnRandomMove.getX()
            // + "Goal pos y= " + doorOnRandomMove.getY());
            // System.out.println("----------------------------------");
            //
            //
            // if (newPosition.equals(doorOnRandomMove))
            // {
            // // this.setDoorOnRandomMove();
            // this.setDoorOnRandomMove();
            // this.currentRoom.removePlayer(this);
            // this.currentRoom = currentRoom.getDoors().get(newPosition);
            // this.currentRoom.addMonster(this);
            //
            // }
            //
            // this.position = newPosition;
            //
            // this.notifyObservers(new PlayerMoved(newPosition));
            //
            // break;
        }

    }

    private boolean isValidPosition(Point desiredPosition,
            MovementPattern movementPattern)
    {
        boolean returnValue = false;

        switch (movementPattern)
        {

        case HERO_IN_ROOM:

            // Has to be in the room
            returnValue = this.currentRoom.isInside(desiredPosition)
                    // Cannot move to a door unless hero is present on the door
                    // or
                    // in the room.
                    && (!Dungeon.getInstance().isDoor(desiredPosition)
                            || desiredPosition.equals(Dungeon.getInstance()
                                    .getHero().getPosition()) || this.currentRoom
                                .isInside(Dungeon.getInstance().getHero()
                                        .getPosition()))
                    // Can not move to a position with a monster
                    && !Dungeon.getInstance().isMonsterOnPosition(
                            desiredPosition);
            break;

        case HERO_IN_NEXT_ROOM:
            // Has to be door or inside room the room andalso there cant be a
            // monster present
            returnValue = (this.currentRoom.checkForDoor(desiredPosition) || this.currentRoom
                    .isInside(desiredPosition))

            // Can not move to a position with a monster
                    && !Dungeon.getInstance().isMonsterOnPosition(
                            desiredPosition);

            break;
        case DEFAULT:
            // Has to be in the room
            returnValue = (this.currentRoom.checkForDoor(desiredPosition) || this.currentRoom
                    .isInside(desiredPosition));

            // If monsters meet n defualt walk they pass through eatch other
            // && !Dungeon.getInstance().isMonsterOnPosition(desiredPosition);

            break;
        }

        return returnValue;

    }

    private int manhattenDistance(Point from, Point to)
    {
        return Math.abs(from.getX() - to.getX())
                + Math.abs(from.getY() - to.getY());
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

    // TODO Must not be same door
    public void setDoorOnRandomMove()
    {
        int size = this.currentRoom.getDoors().size();
        int item = new Random().nextInt(size);
        int i = 0;
        for (Point p : this.currentRoom.getDoors().keySet())
        {
            if (i == item)
                this.doorOnRandomMove = p;
            i++;
        }
    }

    public void setdoorOnRandomMove(Point currentPosition, Point newPosition)
    {

        // laver man en ny eller kopiere den?
        Set<Point> avalibleRooms = this.currentRoom.getDoors().get(newPosition)
                .getDoors().keySet();

        if (avalibleRooms.size() < 0)
        {
            avalibleRooms.remove(currentPosition);
        }
        int size = this.currentRoom.getDoors().size();
        int item = new Random().nextInt(size);
        int i = 0;
        for (Point p : avalibleRooms)
        {
            if (i == item)
                this.doorOnRandomMove = p;
            i++;
        }
    }

    public void pickRandomDoor()
    {
        int size = Dungeon.getInstance().allDoors.size();
        int item = new Random().nextInt(size);
        this.doorOnRandomMove = Dungeon.getInstance().allDoors.get(item);

    }
}
