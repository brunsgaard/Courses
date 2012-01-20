//msn378
package game.model.parser;

import game.model.Dungeon;
import game.model.Point;
import game.model.Room;
import game.model.items.Armor;
import game.model.items.Item;
import game.model.items.Potion;
import game.model.items.Weapon;
import game.model.players.Monster;
import game.model.players.monsters.Bat;
import game.model.players.monsters.Goblin;
import game.model.players.monsters.Orc;

import java.io.InputStream;
import java.util.Scanner;

public class DungeonParser
{
    private Scanner in;

    public void parseMapFile(InputStream input)
    {
        this.in = new Scanner(input);

        while (in.hasNext())
        {

            if (in.hasNextInt())
                Dungeon.getInstance().setPointScaleFactor(in.nextInt());

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
    }

    public void addRoom(int x1, int y1, int x2, int y2)
    {
        Dungeon.getInstance().addRoom(
                new Room(new Point(x1, y1), new Point(x2, y2)));
    }

    public void addDoor(int x1, int y1, int x2, int y2)
    {
        Point point1 = new Point(x1, y1);
        Point point2 = new Point(x2, y2);
        Room point1Room = null;
        Room point2Room = null;

        for (Room r : Dungeon.getInstance().getRooms())
        {
            if (r.isInside(point1))
                point1Room = r;
            if (r.isInside(point2))
                point2Room = r;
        }
        point1Room.getDoors().put(point2, point2Room);
        point2Room.getDoors().put(point1, point1Room);
    }

    public void addMonster(int x, int y, String monsterType)
    {
        Room room = null;
        Point position = new Point(x, y);

        for (Room r : Dungeon.getInstance().getRooms())
        {
            if (r.isInside(position))
                room = r;
        }
        Monster monster = null;

        switch (MonsterSymbols.valueOf(monsterType))
        {
        case O:
            monster = new Orc(new Point(x, y));
            break;
        case G:
            monster = new Goblin(new Point(x, y));
            break;
        case B:
            monster = new Bat(new Point(x, y));
            break;
        }
        if (monster != null && room != null)
        {
            room.getMonsters().add(monster);
        }

    }

    public void addItem(int x, int y, String itemType, int damageAttribute)
    {
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
        case P:
            item = new Potion(itemPosition, damageAttribute);
            break;
        }
        if (item != null && itemRoom != null)
            itemRoom.getItems().put(new Point(x, y), item);

    }

}
