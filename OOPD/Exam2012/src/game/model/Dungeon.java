//msn378
package game.model;

import game.model.players.Hero;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Base class for the model component of the game. This class is constructed as
 * a Singleton and has a collection of all the rooms in the Dungeon, the
 * pointScaleFactor, the hero, and the game description.
 * 
 * Because the collection with the items are in the Room class, this function
 * have been moved there.
 */

public class Dungeon
{
    private String description;
    private Hero hero;

    private ArrayList<Room> rooms;
    private int pointScaleFactor;
    private static Dungeon instance = null;
    private Point heroStartPosition;

    private Dungeon()
    {
        this.rooms = new ArrayList<Room>();
        this.setHeroStartPosition(new Point(1, 1));
        
    }

    public static Dungeon getInstance()
    {
        if (Dungeon.instance == null)
            Dungeon.instance = new Dungeon();
        return Dungeon.instance;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Hero getHero()
    {
        return hero;
    }

    /**
     * Sets the hero in the Room present at (0,0).
     */
    public void setHero(Hero hero)
    {
        this.hero = hero;
        this.hero
                .setCurrentRoom(Dungeon.getInstance().getRoom(new Point(0, 0)));
    }

    public ArrayList<Room> getRooms()
    {
        return rooms;
    }

    public Room getRoom(Point p)
    {
        for (Room r : this.getRooms())
        {
            if (r.isInside(p))
                return r;
        }
        return null;
    }

    /**
     * @return Bounds holding size of the dungeon.
     */
    public Bounds getBounds()
    {
        int maxX = -1;
        int maxY = -1;
        for (Room r : this.getRooms())
        {
            Bounds b = r.getBounds();
            if (b.getBottomRight().getX() > maxX)
                maxX = b.getBottomRight().getX();
            if (b.getBottomRight().getY() > maxY)
                maxY = b.getBottomRight().getY();
        }
        return new Bounds(new Point(0, 0), new Point(maxX, maxY));
    }

    public void addRoom(Room room)
    {
        this.rooms.add(room);
    }

    public int getPointScaleFactor()
    {
        return pointScaleFactor;
    }

    public void setPointScaleFactor(int pointScaleFactor)
            throws InputMismatchException
    {
        if (pointScaleFactor <= 0)
        {
            throw new InputMismatchException();
        } else
        {
            this.pointScaleFactor = pointScaleFactor;
        }
    }

    public Point getHeroStartPosition()
    {
        return heroStartPosition;
    }

    public void setHeroStartPosition(Point heroStartPosition)
    {
        this.heroStartPosition = heroStartPosition;
    }

}