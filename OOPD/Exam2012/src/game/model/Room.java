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

    // TODO: Not sure removePlayer is implemented correct.
    public void removePlayer(Player player)
    {
        this.player = null;
    }

    public boolean isInside(Point inputPoint)
    {
        boolean insideX = this.topLeft.getX() <= inputPoint.getX()
                && inputPoint.getX() < this.bottomRight.getX();
        boolean insideY = topLeft.getY() <= inputPoint.getY()
                && inputPoint.getY() < bottomRight.getY();
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
    
    public Monster getMonsterIfPresent(Point position){
        
        for (Monster m : this.monsters ){
            if (m.getPosition().equals(position)) return m;
        }
        return null;
        
    }
    
    public Item loot(Point position){
        
        for (Item i : this.items ){
            if (i.getPosition().equals(position)) return i;
        }
        return null;
        
    }
}