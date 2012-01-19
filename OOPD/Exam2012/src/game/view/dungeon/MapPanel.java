package game.view.dungeon;

import game.controller.Observer;
import game.model.Bounds;
import game.model.Dungeon;
import game.model.Point;
import game.model.Room;
import game.model.items.Item;
import game.model.notification.INotification;
import game.model.notification.PlayerDied;
import game.model.notification.PlayerMoved;
import game.model.players.Monster;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import javax.swing.JPanel;
import javax.swing.RepaintManager;




public class MapPanel extends JPanel implements Observer<INotification>
{
    private static final long serialVersionUID = -539579546590467691L;

    private BufferedImage map;
    private Graphics2D gfx;
    private AffineTransform scaler;
    private Raster dungeonOnly;

    public MapPanel()
    {
        super(new BorderLayout());

        // Observe our dear hero
        Dungeon.getInstance().getHero().addObserver(this);
        
        // Observe all monsters
        for (Room r : Dungeon.getInstance().getRooms()) {
            if (r.getMonsters() == null) continue;
            for (Monster m : r.getMonsters()) {
                m.addObserver(this);
            }
        }

        // How big an image is needed?
        Bounds dungeonBounds = Dungeon.getInstance().getBounds();
        int scaleFactor = Dungeon.getInstance().getPointScaleFactor();
        int width = dungeonBounds.getBottomRight().getX() * scaleFactor + 1;
        int height = dungeonBounds.getBottomRight().getY() * scaleFactor + 1;

        // Use a BufferedImage to represent the dungeon map
        this.map = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        this.setPreferredSize(new Dimension(width, height));
        this.gfx = this.map.createGraphics(); // Returns the improved Graphics2D

        // Use AffineTransform to do all upscaling
        this.scaler = new AffineTransform();
        this.scaler.scale(scaleFactor, scaleFactor);
        this.gfx.setTransform(this.scaler);

        // Generate the raster and finally draw the map
        this.generateRaster();
        this.drawMap();
    }

    /**
     * Draw a dungeon with only stationary objects, such as walls and doors.
     * Stores the result in a re-useable raster for performance
     */
    private void generateRaster()
    {
        // Deal with the scale
        int scaleFactor = Dungeon.getInstance().getPointScaleFactor();
        Double strokeWidth = 1.0 / scaleFactor; // get the minimum stroke width

        double doorAddition = 1.0 - strokeWidth;

        // Draw walls
        this.gfx.setColor(Color.WHITE);
        this.gfx.setStroke(new BasicStroke(strokeWidth.floatValue()));
        for (Room r : Dungeon.getInstance().getRooms())
        {
            Bounds b = r.getBounds();
            this.gfx.draw(new Rectangle(b.getTopLeft().getX(), b.getTopLeft()
                    .getY(), b.getWidth(), b.getHeight()));
        }

        // Draw doors
        this.gfx.setColor(new Color(0x666666));
        for (Room r : Dungeon.getInstance().getRooms())
        {
            for (Point p : r.getDoors().keySet())
            {
                this.gfx.draw(new Line2D.Double(p.getX() + strokeWidth, p
                        .getY() + strokeWidth, p.getX() + doorAddition, p
                        .getY() + doorAddition));
            }
        }
        this.dungeonOnly = this.map.getData();
    }

    /**
     * (re-)draw the dungeon map, using the previous generated raster and
     * overlaying any current info such as hero positions and monsters.
     */
    private void drawMap()
    {
        this.map.setData(this.dungeonOnly);

        for (Room r : Dungeon.getInstance().getRooms())
        {
            if (r.getMonsters() != null) {
                this.gfx.setColor(Color.RED);
                for (Monster m : r.getMonsters())
                {
                    this.gfx.fill(new Rectangle(m.getPosition().getX(), m
                            .getPosition().getY(), 1, 1));
                }
            }
            if (r.getItems() != null) {
                this.gfx.setColor(Color.GREEN);
                for (Item i : r.getItems())
                {
                    this.gfx.fill(new Rectangle(i.getPosition().getX(), i
                            .getPosition().getY(), 1, 1));
                }
            }
        }
        
        Point heroPos = Dungeon.getInstance().getHero().getPosition();
        this.gfx.setColor(Color.BLUE);
        this.gfx.fill(new Rectangle(heroPos.getX(), heroPos.getY(), 1, 1));
    }

    public void update(PlayerMoved change)
    {
        RepaintManager.currentManager(this).markCompletelyDirty(this);
    }
    
    public void update(PlayerDied change)
    {
        RepaintManager.currentManager(this).markCompletelyDirty(this);
    }

    public void update(INotification change)
    {
        if (change instanceof PlayerMoved)
            this.update((PlayerMoved) change);
        if (change instanceof PlayerDied)
            this.update((PlayerDied) change);
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        Dimension s = this.getSize();
        int xOffset = Math.max(0,
                (int) Math.ceil((s.getWidth() - this.map.getWidth()) / 2));
        int yOffset = Math.max(0,
                (int) Math.ceil((s.getHeight() - this.map.getHeight()) / 2));
        graphics.fillRect(0, 0, (int) s.getWidth(), (int) s.getHeight());
        this.drawMap();
        graphics.drawImage(this.map, xOffset, yOffset, null);
    }
}
