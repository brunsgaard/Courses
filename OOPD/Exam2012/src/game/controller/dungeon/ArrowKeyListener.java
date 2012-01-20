//msn378
package game.controller.dungeon;

import game.model.Dungeon;
import game.view.dungeon.DungeonPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ArrowKeyListener implements KeyListener
{
    private DungeonPanel panel;
    
    public ArrowKeyListener(DungeonPanel panel)
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
        if (this.panel.isInventoryOpen()) return;
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP)
        {
            if (Dungeon.getInstance().getHero().tryMove(Direction.NORTH))
                TurnController.getInstance().doTurn();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN)
        {
            if (Dungeon.getInstance().getHero().tryMove(Direction.SOUTH))
                TurnController.getInstance().doTurn();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if (Dungeon.getInstance().getHero().tryMove(Direction.WEST))
                TurnController.getInstance().doTurn();
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if (Dungeon.getInstance().getHero().tryMove(Direction.EAST))
                TurnController.getInstance().doTurn();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent)
    {
        // Auto-generated method stub
    }
}
