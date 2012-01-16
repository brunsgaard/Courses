package model;

import java.util.ArrayList;

import model.players.Hero;
import model.players.Monster;

public class Dungeon
{
    private String description;
    private Hero hero;
    private ArrayList<Monster> monsters;
    private ArrayList<Room> rooms;

    public Dungeon(String description)
    {
        this.description = description;
        this.monsters = new ArrayList();
        this.rooms = new ArrayList();
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

    public ArrayList<Monster> getMonsters()
    {
        return monsters;
    }

    public void setMonsters(ArrayList<Monster> monsters)
    {
        this.monsters = monsters;
    }

    public ArrayList<Room> getRooms()
    {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms)
    {
        this.rooms = rooms;
    }

}