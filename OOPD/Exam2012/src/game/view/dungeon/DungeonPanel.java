package game.view.dungeon;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

        JScrollPane roomScrollPane = new JScrollPane(this.room);
        this.pane.addTab("Room", roomScrollPane);
        this.pane.setMnemonicAt(0, KeyEvent.VK_R);
        
        JScrollPane mapScrollPane = new JScrollPane(this.map);
        this.pane.addTab("Map", mapScrollPane);
        this.pane.setMnemonicAt(1, KeyEvent.VK_M);
        
        this.setPreferredSize(new Dimension(500,400));
        this.add(pane);
        

    }
}
