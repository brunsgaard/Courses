package model;

import java.util.ArrayList;
import java.util.InputMismatchException;

import model.players.Hero;

public class Dungeon
{

    private String description;
    private Hero hero;

    private ArrayList<Room> rooms;
    private int pointScaleFactor;

    public Dungeon()
    {

        this.rooms = new ArrayList<Room>();

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

    public void addRoom(Room room){
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