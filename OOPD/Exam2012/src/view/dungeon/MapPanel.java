package view.dungeon;

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

import javax.swing.JPanel;

import model.Bounds;
import model.Dungeon;
import model.Point;
import model.Room;

public class MapPanel extends JPanel
{   
    private static final long serialVersionUID = -539579546590467691L;
    
    BufferedImage map;
    Graphics2D gfx;
    AffineTransform scaler;
    
    public MapPanel()
    {
        super(new BorderLayout());
        Bounds dungeonBounds = Dungeon.getInstance().getBounds();
        int scaleFactor = Dungeon.getInstance().getPointScaleFactor();
        int width = dungeonBounds.getBottomRight().getX()*scaleFactor+1;
        int height = dungeonBounds.getBottomRight().getY()*scaleFactor+1;
        
        this.map = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        this.setPreferredSize(new Dimension(width,height));
        this.gfx = this.map.createGraphics();
        this.scaler = new AffineTransform();
        this.scaler.scale(scaleFactor, scaleFactor);
        this.gfx.setTransform(this.scaler);
        this.drawMap();
    }
    
    private void drawMap()
    {
        // Deal with the scale
        int scaleFactor = Dungeon.getInstance().getPointScaleFactor();
        Double strokeWidth = 1.0/scaleFactor; // get the minimum stroke width
        double doorAddition = 1.0-strokeWidth;
        
        this.gfx.setStroke(new BasicStroke(strokeWidth.floatValue()));
        for (Room r : Dungeon.getInstance().getRooms()) {
            Bounds b = r.getBounds();
            this.gfx.draw(new Rectangle(b.getTopLeft().getX(),b.getTopLeft().getY(),b.getWidth(),b.getHeight()));
            
        }
        this.gfx.setColor(new Color(0x666666));
        for (Room r : Dungeon.getInstance().getRooms()) {
            for (Point p : r.getDoors().keySet()) {
                this.gfx.draw(new Line2D.Double(p.getX()+strokeWidth,p.getY()+strokeWidth,p.getX()+doorAddition,p.getY()+doorAddition));
            }
        }
        Point heroPos = Dungeon.getInstance().getHero().getPosition();
        this.gfx.setColor(new Color(0x0000ff));
        this.gfx.fill(new Rectangle(heroPos.getX(),heroPos.getY(),1,1));
    }
    
    @Override
    public void paintComponent(Graphics graphics)
    {
        Dimension s = this.getSize();
        int xOffset = Math.max(0,(int) Math.ceil((s.getWidth()-this.map.getWidth())/2));
        int yOffset = Math.max(0,(int) Math.ceil((s.getHeight()-this.map.getHeight())/2));
        graphics.fillRect(0, 0, (int) s.getWidth(), (int) s.getHeight());
        graphics.drawImage(this.map, xOffset, yOffset, null);
    }
}
