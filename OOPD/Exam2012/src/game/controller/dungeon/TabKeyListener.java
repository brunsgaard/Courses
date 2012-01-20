//msn378
package game.controller.dungeon;

import game.view.dungeon.DungeonPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TabKeyListener implements KeyListener
{
    private DungeonPanel panel;
    
    public TabKeyListener(DungeonPanel panel)
    {
        this.panel = panel;
    }
    
    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        // Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
        if (keyEvent.getKeyCode() == KeyEvent.VK_I)
        {
            this.panel.showInventory();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_M)
        {
            this.panel.showMap();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_R)
        {
            this.panel.showRoom();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent)
    {
        // Auto-generated method stub
    }
}
