package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import model.Dungeon;
import model.Point;
import model.Room;
import model.items.Armor;
import model.items.Item;
import model.items.Weapon;
import model.players.Monster;
import model.players.monsters.Goblin;
import model.players.monsters.Orc;
import parser.ItemSymbols;
import parser.MonsterSymbols;
import parser.Params;

public class DungeonParser
{
    private Scanner in;

    public DungeonParser()
    {
    }

    public void parseMapFile()

    {
        try
        {
            this.in = new Scanner(new File(System.getProperty("user.home")
                    + "/dungeon.map"));


            // first line in .map must be pointScaleFactor due to no identifier String
            Dungeon.getInstance().setPointScaleFactor(in.nextInt());

            while (in.hasNext())
            {
                String token = in.next();
                switch (Params.valueOf(token.substring(0, token.length() - 1)))
                {
                case room:

                    this.addRoom(in.nextInt(), in.nextInt(), in.nextInt(),
                            in.nextInt());
                    break;
                case door:

                    this.addDoor(in.nextInt(), in.nextInt(), in.nextInt(),
                            in.nextInt());
                    break;
                case monster:

                    this.addMonster(in.nextInt(), in.nextInt(), in.next());
                    break;
                case item:
                    this.addItem(in.nextInt(), in.nextInt(), in.next(),
                            in.nextInt());

                    break;
                }

            }
        } catch (FileNotFoundException e1)
        {
            System.out.println("Woops.. something went wrong while"
                    + " loading dungeon.map, please place a "
                    + "valid dungeon.map file in your homedir ");

        }
    }

    public void addRoom(int x1, int y1, int x2, int y2)
    {
        Dungeon.getInstance().addRoom(new Room(new Point(x1, y1), new Point(x2, y2)));
    }

    // TODO rewrite function do to lack of readability and smartness
    public void addDoor(int x1, int y1, int x2, int y2)
    {

        Point point1 = new Point(x1, y1);
        Point point2 = new Point(x2, y2);
        Room point1Room = new Room(point1, point2); // NOTE: set too null and
                                                    // your are unhappy
        Room point2Room = new Room(point2, point1);

        for (Room r : Dungeon.getInstance().getRooms())
        {
            if (r.isInside(point1))
                point1Room = r;
            if (r.isInside(point2))
                point2Room = r;
        }
        point1Room.addDoor(point2, point2Room);
        point2Room.addDoor(point1, point1Room);

    }

    public void addMonster(int x, int y, String monsterType)
    {
        // System.out.println(x+y+monsterType);
        Point monsterPosition = new Point(x, y);
        Room monsterRoom = null;
        for (Room r : Dungeon.getInstance().getRooms())
        {
            if (r.isInside(monsterPosition))
                monsterRoom = r;
        }
        Monster monster = null;

        switch (MonsterSymbols.valueOf(monsterType))
        {
        case O:
            monster = new Orc(monsterPosition);
            break;
        case G:
            monster = new Goblin(monsterPosition);
            break;
        }
        if (monster != null && monsterRoom != null)
            monsterRoom.addMonster(monster);

    }

    public void addItem(int x, int y, String itemType, int damageAttribute)
    {
        // System.out.println(x+y+itemType+damageAttribute);
        Point itemPosition = new Point(x, y);
        Room itemRoom = null;
        for (Room r : Dungeon.getInstance().getRooms())
        {
            if (r.isInside(itemPosition))
                itemRoom = r;
        }
        Item item = null;

        switch (ItemSymbols.valueOf(itemType))
        {
        case W:
            item = new Weapon(itemPosition, damageAttribute);
            break;
        case A:
            item = new Armor(itemPosition, damageAttribute);
            break;
        }
        if (item != null && itemRoom != null)
            itemRoom.addItem(item);

    }

}
