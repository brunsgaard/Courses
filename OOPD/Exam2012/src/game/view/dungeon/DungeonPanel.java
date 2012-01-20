package game.view.dungeon;

import game.view.Language;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class DungeonPanel extends JPanel
{
    private static final long serialVersionUID = 6591358346723891464L;

    JTabbedPane pane;
    RoomPanel room;
    MapPanel map;
    StatsPanel stats;
    InventoryPanel inventory;

    public DungeonPanel()
    {
        super(new BorderLayout());
        this.pane = new JTabbedPane();

        this.room = new RoomPanel();
        this.map = new MapPanel();
        this.inventory = new InventoryPanel();
        this.room.setFocusable(false);
        this.map.setFocusable(false);
//        this.inventory.setFocusable(false);

        JScrollPane roomScrollPane = new JScrollPane(this.room);
        this.pane.addTab(Language.DUNGEON_PANEL_ROOM, roomScrollPane);

        JScrollPane mapScrollPane = new JScrollPane(this.map);
        this.pane.addTab(Language.DUNGEON_PANEL_MAP, mapScrollPane);
        
        JScrollPane inventoryScrollPane = new JScrollPane(this.inventory);
        this.pane.addTab(Language.DUNGEON_PANEL_INVENTORY, inventoryScrollPane);
        
        this.stats = new StatsPanel();

        this.setPreferredSize(new Dimension(500, 400));
        this.add(pane, BorderLayout.CENTER);
        this.add(this.stats, BorderLayout.PAGE_END);

    }
}
