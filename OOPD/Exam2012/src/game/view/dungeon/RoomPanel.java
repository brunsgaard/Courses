package game.view.dungeon;

import game.controller.Observer;
import game.controller.dungeon.Direction;
import game.model.Bounds;
import game.model.Dungeon;
import game.model.Point;
import game.model.Room;
import game.model.items.Armor;
import game.model.items.Item;
import game.model.items.Weapon;
import game.model.notification.ChangeRoom;
import game.model.notification.INotification;
import game.model.notification.PlayerDied;
import game.model.notification.PlayerMoved;
import game.model.players.Hero;
import game.model.players.Monster;
import game.model.players.Player;
import game.model.players.heroes.Cleric;
import game.model.players.heroes.Warrior;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

public class RoomPanel extends JPanel implements Observer<INotification>
{
    private static final long serialVersionUID = -4264054111483744630L;
    private static final int tilePixelSize = 32;

    private static final String resourceDirectory = "res";
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

    public RoomPanel()
    {
        super(new BorderLayout());
        this.room = Dungeon.getInstance().getHero().getCurrentRoom();

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
        try
        {
            this.wallTiles = new ArrayList<BufferedImage>();
            for (int i = 0; i < RoomPanel.numWallTiles; i++)
            {
                this.wallTiles.add(ImageIO.read(new File(
                        RoomPanel.resourceDirectory, RoomPanel.wallTileFile + i
                                + ".png")));
            }
            this.floorTiles = new ArrayList<BufferedImage>();
            for (int i = 0; i < RoomPanel.numFloorTiles; i++)
            {
                this.floorTiles.add(ImageIO.read(new File(
                        RoomPanel.resourceDirectory, RoomPanel.floorTileFile
                                + i + ".png")));
            }
            this.doorTile = ImageIO.read(new File(RoomPanel.resourceDirectory,
                    RoomPanel.doorTileFile));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.drawRoom();
    }

    private static final BufferedImage getTile(Player p)
    {
        try
        {
            String filename = p.getClass().getSimpleName().toLowerCase()
                    .concat(".png");
            return ImageIO
                    .read(new File(RoomPanel.resourceDirectory, filename));
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static final BufferedImage getTile(Item i)
    {
        try
        {
            String filename = i.getClass().getSimpleName().toLowerCase()
                    .concat(".png");
            return ImageIO
                    .read(new File(RoomPanel.resourceDirectory, filename));
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
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
        Bounds roomBounds = room.getBounds();
        int width = (roomBounds.getWidth() + 2) * RoomPanel.tilePixelSize;
        int height = (roomBounds.getHeight() + 2) * RoomPanel.tilePixelSize;

        // Use a BufferedImage to represent the dungeon map
        this.roomMap = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        this.setPreferredSize(new Dimension(width, height));
        this.gfx = this.roomMap.createGraphics(); // Returns the improved
                                                  // Graphics2D

        /*
         * Walls are simply drawn around the room with a random tile Floors are
         * drawn inside the room, likewise a random tile. Simplification: The
         * room will look different each time the hero enters //TODO
         */
        Random rand = new Random();
        // Draw walls
        for (int x = 0; x < width; x += RoomPanel.tilePixelSize)
        {
            this.gfx.drawImage(
                    this.wallTiles.get(rand.nextInt(RoomPanel.numWallTiles)),
                    x, 0, null);
            this.gfx.drawImage(
                    this.wallTiles.get(rand.nextInt(RoomPanel.numWallTiles)),
                    x, height - RoomPanel.tilePixelSize, null);
        }
        for (int y = 0; y < height; y += RoomPanel.tilePixelSize)
        {
            this.gfx.drawImage(
                    this.wallTiles.get(rand.nextInt(RoomPanel.numWallTiles)),
                    0, y, null);
            this.gfx.drawImage(
                    this.wallTiles.get(rand.nextInt(RoomPanel.numWallTiles)),
                    width - RoomPanel.tilePixelSize, y, null);
        }
        // Draw floor
        for (int x = 1; x <= roomBounds.getWidth(); x++)
        {
            for (int y = 1; y <= roomBounds.getHeight(); y++)
            {
                this.gfx.drawImage(this.floorTiles.get(rand
                        .nextInt(RoomPanel.numFloorTiles)), x
                        * RoomPanel.tilePixelSize, y * RoomPanel.tilePixelSize,
                        null);
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
                    * RoomPanel.tilePixelSize, doorLocation.getY()
                    * RoomPanel.tilePixelSize, null);
        }
        this.roomOnly = this.roomMap.getData();
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
                this.gfx.drawImage(RoomPanel.getTile(i), itemPos.getX()
                        * RoomPanel.tilePixelSize, itemPos.getY()
                        * RoomPanel.tilePixelSize, null);
            }
        }

        // Draw monsters
        if (this.room.getMonsters() != null)
        {
            for (Monster m : this.room.getMonsters())
            {
                Point monsterPos = this.relativePoint(m.getPosition());
                this.gfx.drawImage(RoomPanel.getTile(m), monsterPos.getX()
                        * RoomPanel.tilePixelSize, monsterPos.getY()
                        * RoomPanel.tilePixelSize, null);
            }
        }

        // Draw hero
        Hero hero = Dungeon.getInstance().getHero();
        Point heroPos = this.relativePoint(hero.getPosition());
        this.gfx.drawImage(RoomPanel.getTile(hero), heroPos.getX()
                * RoomPanel.tilePixelSize, heroPos.getY()
                * RoomPanel.tilePixelSize, null);

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
