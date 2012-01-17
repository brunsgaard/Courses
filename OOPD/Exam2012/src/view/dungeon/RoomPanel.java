package view.dungeon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class RoomPanel extends JPanel
{
    private static final long serialVersionUID = -4264054111483744630L;

    public RoomPanel()
    {
        super(new BorderLayout());
    }
    
    @Override
    public void paintComponent(Graphics graphics)
    {
        Dimension s = this.getSize();
        graphics.fillRect(0, 0, (int) s.getWidth(), (int) s.getHeight());
    }
}
