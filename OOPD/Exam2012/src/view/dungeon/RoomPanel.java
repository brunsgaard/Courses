package view.dungeon;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RoomPanel extends JPanel
{
    private static final long serialVersionUID = -4264054111483744630L;

    public RoomPanel()
    {
        super(new BorderLayout());
        this.add(new JLabel("hello"));
    }
}
