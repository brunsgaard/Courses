package view.dungeon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class DungeonPanel extends JPanel
{
    private static final long serialVersionUID = 6591358346723891464L;
    
    JTabbedPane pane;
    RoomPanel room;
    MapPanel map;

    public DungeonPanel()
    {
        super(new BorderLayout());
        this.pane = new JTabbedPane();
        
        this.room = new RoomPanel();
        this.map = new MapPanel();
        
        this.pane.addTab("Room", room);
        this.pane.addTab("Map", map);
        this.pane.setMnemonicAt(0, KeyEvent.VK_R);
        this.pane.setMnemonicAt(1, KeyEvent.VK_M);
        this.setPreferredSize(new Dimension(500,400));
        this.add(pane);
    }
}
