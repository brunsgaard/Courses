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

    private ArrayList<Item> items;
    private ArrayList<Monster> monsters;
 

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

    // TODO: Not sure removePlayer is implemented correct.
    public void removePlayer(Player player)
    {
        if (player instanceof Monster)
        {
            int index = this.monsters.indexOf(player);
            if (index == -1)
                return;
            this.monsters.remove(index);
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

    public Monster getMonsterFromNewPosition(Point position)
    {

        for (Monster m : this.monsters)
        {
            if (m.getPosition().equals(position))
                return m;
        }
        return null;

    }
    
    public boolean isMonsterOnPosition(Point position)
    {
        if (this.getMonsterFromNewPosition(position) != null) return true;
                
        return false;


    }

    public Item loot(Point position)
    {

        for (Item i : this.items)
        {
            if (i.getPosition().equals(position))
                return i;
        }
        return null;

    }

    public boolean checkForDoor(Point position)
    {

        return this.doors.containsKey(position);

    }

}
