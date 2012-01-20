//msn378
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

/**
 * Base class for all Monsters. Implements monster movement and damage. Monsters
 * will move according to three different patterns; DEFAULT, HERO_IN_ROOM or
 * HERO_IN_NEXT_ROOM. The default pattern picks a door at random and moves
 * through it. HERO_IN* moves towards the hero, eventually damaging the hero.
 */
public abstract class Monster extends Player
{
    public enum MovementPattern
    {
        DEFAULT, HERO_IN_ROOM, HERO_IN_NEXT_ROOM
    }

    private Point doorOnRandomMove;
    private MovementPattern movementPattern;

    public Monster(Point position, int unarmedDamage,
            int healthRegenerationRate, int hitPoints)
    {
        super(position, unarmedDamage, healthRegenerationRate, hitPoints);
        this.room = Dungeon.getInstance().getRoom(position);
        this.doorOnRandomMove = null;
        this.setRandomDoorAsTarget();
        this.movementPattern = MovementPattern.DEFAULT;
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

    /**
     * Moves the monster autonomously by following these steps. 1) Selects a
     * movement pattern and target based on hero location. 2) calculate the
     * optimal movement direction. 3) perform move.
     */
    public void makeAutonomousMove()
    {
        Hero hero = Dungeon.getInstance().getHero();

        // Initialize
        Direction moveDirection = null;
        Point endPosition = null;
        Point target = null;

        // Select movement pattern and set target
        if (this.room.isInside(hero.getPosition()))
        {
            this.movementPattern = MovementPattern.HERO_IN_ROOM;
            target = hero.getPosition();
        } else if (Room.isInNeighborRoom(room, hero.getPosition()))
        {
            this.movementPattern = MovementPattern.HERO_IN_NEXT_ROOM;
            for (Point p : this.room.getDoors().keySet())
            {
                if (hero.getCurrentRoom().isInside(p))
                {
                    target = p;
                    break;
                }
            }
        } else
        {
            target = this.doorOnRandomMove;
        }

        // Calculate optimal direction
        moveDirection = this.getDirection(target);
        if (moveDirection == null)
            return; // stay put

        // Perform move
        endPosition = Point.oneStep(this.position, moveDirection);
        if (endPosition.equals(hero.getPosition()))
        {
            hero.takeDamage(this.getDamageLevel());
            return;
        }

        // If position is door and it's the one we're looking for change room
       // if (Dungeon.getInstance().isDoor(endPosition)
        if (this.room.checkForDoor(endPosition)
                && endPosition.equals(target))
        {
            this.autonomousChangeRoom(endPosition);
        }

        // Set new position and notify
        this.position = endPosition;
        this.notifyObservers(new PlayerMoved(endPosition));
    }

    /**
     * Check if: 1) the desired position is a door OR inside room 2) there isn't
     * another monster there already. Special case: if hero is standing on the
     * door don't go through it, instead wait for him to leave.
     */
    private boolean isValidPosition(Point desiredEndPosition)
    {
        if (this.movementPattern == MovementPattern.HERO_IN_NEXT_ROOM
                && Dungeon.getInstance().getHero().getPosition()
                        .equals(desiredEndPosition))
            return false;
        return (this.room.checkForDoor(desiredEndPosition) || this.room
                .isInside(desiredEndPosition))
                && !Room.isMonsterOnPosition(room, desiredEndPosition);
    }

    /**
     * Calculate manhatten distance for all four directions if the direction
     * results in a valid position.
     * 
     * @return optimal direction or null if the optimal is to stay put
     */
    private Direction getDirection(Point position)
    {
        Point pn = Point.oneStep(this.position, Direction.NORTH);
        Point ps = Point.oneStep(this.position, Direction.SOUTH);
        Point pe = Point.oneStep(this.position, Direction.EAST);
        Point pw = Point.oneStep(this.position, Direction.WEST);
        int nd = this.manhattenDistance(position, pn);
        int sd = this.manhattenDistance(position, ps);
        int ed = this.manhattenDistance(position, pe);
        int wd = this.manhattenDistance(position, pw);

        Direction shortestDirection = null;
        int shortestDistance = this.manhattenDistance(this.position, position);
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

    private int manhattenDistance(Point from, Point to)
    {
        return Math.abs(from.getX() - to.getX())
                + Math.abs(from.getY() - to.getY());
    }

    /**
     * Before the monster changes rooms, examine the new room's door's and
     * remove the door it came from unless it's the only door. Chooses a new
     * random door for the default move pattern. Also updates the monster's
     * current room.
     */
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
        // newRoom.addMonster(this);
        // is newPosition correct?
        newRoom.getMonsters().add(this);
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
