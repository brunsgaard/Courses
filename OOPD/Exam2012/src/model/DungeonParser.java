package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import model.players.Monster;
import model.players.monsters.Goblin;
import model.players.monsters.Orc;

public class DungeonParser
{

    private Scanner in;
    private Dungeon dungeon;

    public DungeonParser(File file, Dungeon dungeon)
            throws FileNotFoundException
    {
        try
        {
            this.in = new Scanner(file);
        } catch (FileNotFoundException e)
        {
            this.in = new Scanner(new File(System.getProperty("user.home")
                    + "/dungeon.map"));
        }
        this.dungeon = dungeon;
    }

    public Dungeon parse() throws FileNotFoundException
    {
        // first line in .map must be pointScaleFactor
        this.dungeon.setPointScaleFactor(in.nextInt());
        while (in.hasNext())
        {
            String token = in.next();
            switch (token)
            {
            case "room:":
                this.addRoom(in.nextInt(), in.nextInt(), in.nextInt(),
                        in.nextInt());
            case "door:":
                this.addDoor(in.nextInt(), in.nextInt(), in.nextInt(),
                        in.nextInt());
            case "monster:":
                break;
            case "item:":
                break;
            }

        }
        return this.dungeon;
    }

    public void addRoom(int x1, int y1, int x2, int y2)
    {
        this.dungeon.getRooms().add(
                new Room(new Point(x1, y1), new Point(x2, y2)));
    }

    public void addDoor(int x1, int y1, int x2, int y2)
    {
        Point point1 = new Point(x1, y1);
        Point point2 = new Point(x2, y2);
        Room roomToFind1 = null;
        Room roomToFind2 = null;

        for (Room r : this.dungeon.getRooms())
        {
            if (r.isInside(point1))
                roomToFind1 = r;
            if (r.isInside(point2))
                roomToFind2 = r;
        }
        roomToFind1.getDoors().put(point2, roomToFind2);
        roomToFind2.getDoors().put(point1, roomToFind1);

    }

    public void addMonstor(int x, int y, Character monsterType)
    {
        Point monsterPosition = new Point(x, y);
        Monster monster = null;
        if (monsterType.equals('G'))
            monster = new Goblin(monsterPosition);
        if (monsterType.equals('O'))
            monster = new Orc(monsterPosition);
        if (monster != null) this.dungeon.getMonsters().add(monster);

    }

    public void additem(int pointScaleFactor)
    {

    }

}
