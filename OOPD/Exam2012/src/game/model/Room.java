package game.model;

import game.model.items.Item;
import game.model.players.Monster;
import game.model.players.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Room
{
    private Point topLeft;
    private Point bottomRight;
    private HashMap<Point, Room> doors;

    private HashMap<Point, Item> items;
    private ArrayList<Monster> monsters;

    public Room(Point topLeft, Point bottomRight)
    {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.doors = new HashMap<Point, Room>();
        this.items = new HashMap<Point, Item>();
        this.monsters = new ArrayList<Monster>();
    }

    // method name and input names forced
    // by exam description.
    public void removePlayer(Player player)
    {
        if (player instanceof Monster)
        {
            this.getMonsters().remove(player);
        }
    }

    public boolean isInside(Point inputPoint)
    {
        boolean insideX = this.topLeft.getX() <= inputPoint.getX()
                && inputPoint.getX() < this.bottomRight.getX();
        boolean insideY = topLeft.getY() <= inputPoint.getY()
                && inputPoint.getY() < bottomRight.getY();
        return insideX && insideY;
    }

    public Bounds getBounds()
    {
        return new Bounds(this.topLeft, this.bottomRight);
    }

    public static boolean isInNeighborRoom(Room room, Point position)
    {
        if (Room.getNeighborRoomFromPoint(room, position) != null)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public static Room getNeighborRoomFromPoint(Room room, Point position)
    {
        for (Room r : room.getDoors().values())
        {
            if (r.isInside(position))
                return r;
        }
        return null;
    }

    public static Item loot(Room room, Point position)
    {
        Item item = room.getItems().get(position);
        room.getItems().remove(position);
        return item;

    }

    public boolean checkForDoor(Point position)
    {
        return this.doors.containsKey(position);
    }

    public static Monster getMonsterFromPoint(Room room, Point position)
    {
        for (Monster m : room.getMonsters())
        {
            if (m.getPosition().equals(position))
                return m;
        }
        for (Room r : room.getDoors().values())
        {
            for (Monster m : r.getMonsters())
            {
                if (m.getPosition().equals(position))
                    return m;
            }
        }
        return null;
    }

    public static boolean isMonsterOnPosition(Room room, Point position)
    {
        if (Room.getMonsterFromPoint(room, position) != null)
            return true;
        return false;
    }

    public HashMap<Point, Room> getDoors()
    {
        return doors;
    }

    public void setDoors(HashMap<Point, Room> doors)
    {
        this.doors = doors;
    }

    public HashMap<Point, Item> getItems()
    {
        return items;
    }

    public void setItems(HashMap<Point, Item> items)
    {
        this.items = items;
    }

    public ArrayList<Monster> getMonsters()
    {
        return monsters;
    }

    public void setMonsters(ArrayList<Monster> monsters)
    {
        this.monsters = monsters;
    }
}