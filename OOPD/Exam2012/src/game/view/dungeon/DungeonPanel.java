//msn378
package game.view.dungeon;

import game.view.Language;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Panel with three tabbed scroll panes containing; Room, Map and Inventory.
 * Also adds a stats bar to the bottom
 */
public class DungeonPanel extends JPanel
{
    private static final long serialVersionUID = 6591358346723891464L;

    JTabbedPane pane;
    RoomPanel room;
    JScrollPane roomScroll;
    MapPanel map;
    JScrollPane mapScroll;
    StatsPanel stats;
    InventoryPanel inventory;
    JScrollPane inventoryScroll;

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

        this.roomScroll = new JScrollPane(this.room);
        this.mapScroll = new JScrollPane(this.map);
        this.inventoryScroll = new JScrollPane(this.inventory);
        
        this.pane.addTab(Language.DUNGEON_PANEL_ROOM, this.roomScroll);
        this.pane.addTab(Language.DUNGEON_PANEL_MAP, this.mapScroll);
        this.pane.addTab(Language.DUNGEON_PANEL_INVENTORY, this.inventoryScroll);
        
        this.stats = new StatsPanel();

        this.setPreferredSize(new Dimension(500, 400));
        this.add(pane, BorderLayout.CENTER);
        this.add(this.stats, BorderLayout.PAGE_END);
    }
    
    public void showRoom()
    {
        this.pane.setSelectedComponent(this.roomScroll);
    }
    
    public void showMap()
    {
        this.pane.setSelectedComponent(this.mapScroll);
    }
    
    public void showInventory()
    {
        this.pane.setSelectedComponent(this.inventoryScroll);
    }
    
    public boolean isInventoryOpen()
    {
        return this.pane.getSelectedComponent() == this.inventoryScroll;
    }
}
