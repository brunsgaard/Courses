package game.model;

import game.model.players.Hero;

import java.util.ArrayList;
import java.util.InputMismatchException;


public class Dungeon
{

    private String description;
    private Hero hero;

    private ArrayList<Room> rooms;
    private int pointScaleFactor;
    private static Dungeon instance = null;

    private Dungeon()
    {
        this.rooms = new ArrayList<Room>();
    }
    
    public static Dungeon getInstance()
    {
        if (Dungeon.instance == null) Dungeon.instance = new Dungeon();
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

    public void setHero(Hero hero)
    {
        this.hero = hero;
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
    
    public Bounds getBounds()
    {
        int minX = -1;
        int maxX = -1;
        int minY = -1;
        int maxY = -1;
        for (Room r : this.getRooms())
        {
            Bounds b = r.getBounds();
            if (minX == -1 || b.getTopLeft().getX() < minX) minX = b.getTopLeft().getX();
            if (maxX == -1 || b.getBottomRight().getX() > maxX) maxX = b.getBottomRight().getX();
            if (minY == -1 || b.getTopLeft().getY() < minY) minY = b.getTopLeft().getY();
            if (maxY == -1 || b.getBottomRight().getY() > maxY) maxY = b.getBottomRight().getY();
        }
        return new Bounds(new Point(minX, minY), new Point(maxX, maxY));
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

}