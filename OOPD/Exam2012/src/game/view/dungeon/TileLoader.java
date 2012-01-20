package game.view.dungeon;

import game.model.items.Item;
import game.model.players.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Class to load graphic "tiles" based on an object or class. Returns tiles as
 * BufferedImages. Caches loaded images in a HashMap. Completely static for
 * convenience. On error it returns a BufferedImage with a printed error message.
 */
public class TileLoader
{
    public static final String resourceDirectory = "res";
    public static final int tilePixelSize = 32;
    private static HashMap<String, BufferedImage> cache = new HashMap<String, BufferedImage>();

    private static BufferedImage errorTile()
    {
        BufferedImage error = new BufferedImage(TileLoader.tilePixelSize,
                TileLoader.tilePixelSize, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D gfx = error.createGraphics();
        gfx.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        gfx.setBackground(Color.BLACK);
        gfx.setColor(Color.WHITE);
        gfx.drawString("Not", 1, 11);
        gfx.drawString("Found", 1, 25);
        gfx.dispose();
        return error;
    }

    private static BufferedImage cacheLookup(String filename)
    {
        try
        {
            if (TileLoader.cache.containsKey(filename))
                return TileLoader.cache.get(filename);
            URL imageUrl = TileLoader.class.getResource(TileLoader.resourceDirectory+"/"+filename);
            BufferedImage img = ImageIO.read(imageUrl);
            TileLoader.cache.put(filename, img);
            return img;
        } catch (IOException e)
        {
            e.printStackTrace();
            return TileLoader.errorTile();
        }
    }

    public static BufferedImage getTile(String filename)
    {
        return TileLoader.cacheLookup(filename);
    }

    /**
     * Method to get a tile for a class instead of an object. Ie. Mage.class
     */
    public static <T> BufferedImage getTile(Class<T> c)
    {
        return TileLoader.cacheLookup(c.getSimpleName().toLowerCase()
                .concat(".png"));
    }

    public static BufferedImage getTile(Player p)
    {
        return TileLoader.cacheLookup(p.getClass().getSimpleName()
                .toLowerCase().concat(".png"));
    }

    public static BufferedImage getTile(Item i)
    {
        return TileLoader.cacheLookup(i.getClass().getSimpleName()
                .toLowerCase().concat(".png"));
    }
}
