package game.view.dungeon;

import game.controller.Observer;
import game.controller.dungeon.Direction;
import game.controller.notification.ChangeRoom;
import game.controller.notification.INotification;
import game.controller.notification.PlayerDied;
import game.controller.notification.PlayerMoved;
import game.model.Bounds;
import game.model.Dungeon;
import game.model.Point;
import game.model.Room;
import game.model.items.Item;
import game.model.players.Hero;
import game.model.players.Monster;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.RepaintManager;

public class RoomPanel extends JPanel implements Observer<INotification>
{
    private static final long serialVersionUID = -4264054111483744630L;

    private static final String floorTileFile = "pebble_brown";
    private static final int numFloorTiles = 9;
    private static final String wallTileFile = "brick_brown";
    private static final int numWallTiles = 7;
    private static final String doorTileFile = "dngn_closed_door.png";

    private Room room;
    private BufferedImage roomMap;
    private Graphics2D gfx;
    private Raster roomOnly;
    private ArrayList<BufferedImage> floorTiles;
    private ArrayList<BufferedImage> wallTiles;
    private BufferedImage doorTile;
    private HashMap<Room, Raster> roomCache;

    public RoomPanel()
    {
        super(new BorderLayout());
        this.room = Dungeon.getInstance().getHero().getCurrentRoom();
        this.roomCache = new HashMap<Room, Raster>();

        // Observe our dear hero
        Dungeon.getInstance().getHero().addObserver(this);

        // Observe all monsters
        for (Room r : Dungeon.getInstance().getRooms())
        {
            if (r.getMonsters() == null)
                continue;
            for (Monster m : r.getMonsters())
            {
                m.addObserver(this);
            }
        }

        // Load tiles
        this.wallTiles = new ArrayList<BufferedImage>();
        for (int i = 0; i < RoomPanel.numWallTiles; i++)
        {
            this.wallTiles.add(TileLoader.getTile(RoomPanel.wallTileFile + i
                    + ".png"));
        }
        this.floorTiles = new ArrayList<BufferedImage>();
        for (int i = 0; i < RoomPanel.numFloorTiles; i++)
        {
            this.floorTiles.add(TileLoader.getTile(RoomPanel.floorTileFile + i
                    + ".png"));
        }
        this.doorTile = TileLoader.getTile(RoomPanel.doorTileFile);

        this.drawRoom();
    }

    private Point relativePoint(Point p)
    {
        Bounds roomBounds = room.getBounds();
        int x = p.getX() - roomBounds.getTopLeft().getX() + 1;
        int y = p.getY() - roomBounds.getTopLeft().getY() + 1;
        return new Point(x, y);
    }

    private void drawRoom()
    {
        Bounds roomBounds = this.room.getBounds();
        int width = (roomBounds.getWidth() + 2) * TileLoader.tilePixelSize;
        int height = (roomBounds.getHeight() + 2) * TileLoader.tilePixelSize;

        // Use a BufferedImage to represent the dungeon map
        this.roomMap = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        this.setPreferredSize(new Dimension(width, height));
        this.gfx = this.roomMap.createGraphics(); // Returns the improved
                                                  // Graphics2D

        /*
         * Cache lookup. Room uses the default Object.hashCode, but since this
         * is for caching only it's not that important.
         */
        if (this.roomCache.containsKey(this.room))
        {
            this.roomOnly = this.roomCache.get(this.room);
            return;
        }

        /*
         * Walls are simply drawn around the room with a random tile Floors are
         * drawn inside the room, likewise a random tile.
         */
        Random rand = new Random();
        // Draw walls
        for (int x = 0; x < width; x += TileLoader.tilePixelSize)
        {
            this.gfx.drawImage(
                    this.wallTiles.get(rand.nextInt(RoomPanel.numWallTiles)),
                    x, 0, null);
            this.gfx.drawImage(
                    this.wallTiles.get(rand.nextInt(RoomPanel.numWallTiles)),
                    x, height - TileLoader.tilePixelSize, null);
        }
        for (int y = 0; y < height; y += TileLoader.tilePixelSize)
        {
            this.gfx.drawImage(
                    this.wallTiles.get(rand.nextInt(RoomPanel.numWallTiles)),
                    0, y, null);
            this.gfx.drawImage(
                    this.wallTiles.get(rand.nextInt(RoomPanel.numWallTiles)),
                    width - TileLoader.tilePixelSize, y, null);
        }
        // Draw floor
        for (int x = 1; x <= roomBounds.getWidth(); x++)
        {
            for (int y = 1; y <= roomBounds.getHeight(); y++)
            {
                this.gfx.drawImage(this.floorTiles.get(rand
                        .nextInt(RoomPanel.numFloorTiles)), x
                        * TileLoader.tilePixelSize, y
                        * TileLoader.tilePixelSize, null);
            }
        }
        // Draw doors
        for (Point p : this.room.getDoors().keySet())
        {
            // Calculate position in wall
            Point doorLocation = this.relativePoint(p);
            if (p.getX() == 1)
            {
                doorLocation.oneStep(Direction.WEST);
            } else if (p.getY() == 1)
            {
                doorLocation.oneStep(Direction.NORTH);
            } else if (p.getX() == roomBounds.getWidth() + 1)
            {
                doorLocation.oneStep(Direction.EAST);
            } else
            {
                doorLocation.oneStep(Direction.SOUTH);
            }
            this.gfx.drawImage(this.doorTile, doorLocation.getX()
                    * TileLoader.tilePixelSize, doorLocation.getY()
                    * TileLoader.tilePixelSize, null);
        }
        this.roomOnly = this.roomMap.getData();
        this.roomCache.put(this.room, this.roomOnly);
    }

    /**
     * (re-)draw the dungeon map, using the previous generated raster and
     * overlaying any current info such as hero positions and monsters.
     */
    private void drawMap()
    {
        this.roomMap.setData(this.roomOnly);

        // Draw items
        if (this.room.getItems() != null)
        {
            for (Item i : this.room.getItems())
            {
                Point itemPos = this.relativePoint(i.getPosition());
                this.gfx.drawImage(TileLoader.getTile(i), itemPos.getX()
                        * TileLoader.tilePixelSize, itemPos.getY()
                        * TileLoader.tilePixelSize, null);
            }
        }

        // Draw monsters
        if (this.room.getMonsters() != null)
        {
            for (Monster m : this.room.getMonsters())
            {
                Point monsterPos = this.relativePoint(m.getPosition());
                this.gfx.drawImage(TileLoader.getTile(m), monsterPos.getX()
                        * TileLoader.tilePixelSize, monsterPos.getY()
                        * TileLoader.tilePixelSize, null);
            }
        }

        // Draw hero
        Hero hero = Dungeon.getInstance().getHero();
        Point heroPos = this.relativePoint(hero.getPosition());
        this.gfx.drawImage(TileLoader.getTile(hero), heroPos.getX()
                * TileLoader.tilePixelSize, heroPos.getY()
                * TileLoader.tilePixelSize, null);

    }

    public void update(PlayerMoved change)
    {
        RepaintManager.currentManager(this).markCompletelyDirty(this);
    }

    public void update(PlayerDied change)
    {
        RepaintManager.currentManager(this).markCompletelyDirty(this);
    }

    public void update(ChangeRoom change)
    {
        this.room = change.getRoom();
        this.drawRoom();
        RepaintManager.currentManager(this).markCompletelyDirty(this);
    }

    public void update(INotification change)
    {
        if (change instanceof PlayerMoved)
            this.update((PlayerMoved) change);
        if (change instanceof PlayerDied)
            this.update((PlayerDied) change);
        if (change instanceof ChangeRoom)
            this.update((ChangeRoom) change);
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        Dimension s = this.getSize();
        int xOffset = Math.max(0,
                (int) Math.ceil((s.getWidth() - this.roomMap.getWidth()) / 2));
        int yOffset = Math
                .max(0, (int) Math.ceil((s.getHeight() - this.roomMap
                        .getHeight()) / 2));
        graphics.fillRect(0, 0, (int) s.getWidth(), (int) s.getHeight());
        this.drawMap();
        graphics.drawImage(this.roomMap, xOffset, yOffset, null);
    }
}
