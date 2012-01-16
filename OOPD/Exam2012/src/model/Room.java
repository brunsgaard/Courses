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
        this.doors = new HashMap();
        this.items = new ArrayList();
        this.monsters = new ArrayList();
        
    }
   
    public HashMap<Point, Room> getDoors()
    {
        return doors;
    }
    
    public ArrayList<Monster> getMonsters()
    {
        return monsters;
    }

    public void setMonsters(ArrayList<Monster> monsters)
    {
        this.monsters = monsters;
    }

    public void setDoors(HashMap<Point, Room> doors)
    {
        this.doors = doors;
    }

    public ArrayList<Item> getItems()
    {
        return items;
    }

    public void setItems(ArrayList<Item> items)
    {
        this.items = items;
    }
    // TODO: Not sure removePlayer is implemented right.
    public void removePlayer(Player player){
        this.player = null;
    }

    public boolean isInside(Point inputPoint)
    {
        boolean insideX = this.topLeft.getX() <= inputPoint.getX()
                && inputPoint.getX() <= this.bottomRight.getX();
        boolean insideY = bottomRight.getY() <= inputPoint.getY()
                && inputPoint.getY() <= topLeft.getY();
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

}
