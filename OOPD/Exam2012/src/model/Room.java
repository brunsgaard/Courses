package model;

import java.util.ArrayList;
import java.util.HashMap;

import model.items.Item;
import model.players.Monster;
import model.players.Player;

public class Room
{
    private Point topLeft;
    private Point bottomRight;
    private HashMap<Point, Room> doors;
    private ArrayList<Item> items;
    private ArrayList<Monster> monsters;
    private Player player;

    public Room(Point topLeft, Point bottomRight)
    {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.doors = new HashMap<Point, Room>();
        this.items = new ArrayList<Item>();
        this.monsters = new ArrayList<Monster>();

    }

    public HashMap<Point, Room> getDoors()
    {
        return doors;
    }

    public void addDoor(Point point, Room room)
    {
        this.doors.put(point, room);

    }

    public ArrayList<Monster> getMonsters()
    {
        return monsters;
    }

    public void addMonster(Monster monster)
    {
        this.monsters.add(monster);
    }

    public boolean removeMonster(Monster monster)
    {
        int index = this.monsters.indexOf(monster);
        if (index == -1)
            return false;
        this.monsters.remove(index);
        return true;

    }

    public ArrayList<Item> getItems()
    {
        return items;
    }

    public void addItem(Item item)
    {
        this.items.add(item);
    }

    public boolean removeItem(Item item)
    {
        int index = this.items.indexOf(item);
        if (index == -1)
            return false;
        this.items.remove(index);
        return true;

    }

    // TODO: Not sure removePlayer is implemented right.
    public void removePlayer(Player player)
    {
        this.player = null;
    }

    // TODO: is inside? the wall implementation?
    public boolean isInside(Point inputPoint)
    {
        boolean insideX = this.topLeft.getX() <= inputPoint.getX()
                && inputPoint.getX() <= this.bottomRight.getX();
        boolean insideY = topLeft.getY() <= inputPoint.getY()
                && inputPoint.getY() <= bottomRight.getY();
        return insideX && insideY;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }
    
    public Bounds getBounds()
    {
        return new Bounds(this.topLeft, this.bottomRight);
    }
}
