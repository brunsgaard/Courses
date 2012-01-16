package model;

import java.util.ArrayList;
import java.util.HashMap;

import model.items.Item;

public class Room
{
    private Point topLeft;
    private Point bottomRight;
    private HashMap<Point, Room> doors;
    private ArrayList<Item> items;

    public Room(Point topLeft, Point bottomRight)
    {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.doors = new HashMap();
        this.items = new ArrayList();
    }
   
    public HashMap<Point, Room> getDoors()
    {
        return doors;
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

    public boolean isInside(Point inputPoint)
    {
        boolean insideX = this.topLeft.getX() < inputPoint.getX()
                && inputPoint.getX() < this.bottomRight.getX();
        boolean insideY = bottomRight.getY() < inputPoint.getY()
                && inputPoint.getY() < topLeft.getY();
        return insideX && insideY;
    }

}
