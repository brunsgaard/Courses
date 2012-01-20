package game.model;

import game.model.items.Item;
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
    private ArrayList<Point> allDoors;

    private Dungeon()
    {
        this.rooms = new ArrayList<Room>();
        this.allDoors = new ArrayList<Point>();
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

    public Bounds getBounds()
    {
        int maxX = -1;
        int maxY = -1;
        for (Room r : this.getRooms())
        {
            Bounds b = r.getBounds();
            if (maxX == -1 || b.getBottomRight().getX() > maxX)
                maxX = b.getBottomRight().getX();
            if (maxY == -1 || b.getBottomRight().getY() > maxY)
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

    public void addToAllDoors(Point point)
    {
        this.allDoors.add(point);
    }

    public boolean isDoor(Point point)
    {
        return this.allDoors.contains(point);

    }

    public Item loot(Point position)
    {
        // method moved to Room Class. If placed on dungeon
        // the game have for each move search all roomes in the
        // dungeon for items.
        // (because Dungeon is a singelton we could call
        // Dungeon.getInstance.getHero.getCurrentRoom
        // and use this and at the seams more
        // correct to place it the function in the
        // room class)
        // this way the game only have to check one room.
        return null;

    }

//    public Monster getMonsterFromPoint(Point position)
//    {
//        for (Room r : this.rooms)
//        {
//
//            for (Monster m : r.getMonsters().values())
//            {
//                if (m.getPosition().equals(position))
//                    return m;
//            }
//        }
//        return null;
//    }
//
//    public boolean isMonsterOnPosition(Point position)
//    {
//        if (this.getMonsterFromPoint(position) != null)
//        {
//            return true;
//        } else
//        {
//            return false;
//        }
//    }
}