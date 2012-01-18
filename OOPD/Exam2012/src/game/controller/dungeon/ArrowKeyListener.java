package game.controller.dungeon;

import game.controller.dungeon.Direction;
import game.model.Dungeon;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ArrowKeyListener implements KeyListener
{


    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        // Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP)
        {
            Dungeon.getInstance().getHero().tryMove(Direction.NORTH);

        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN)
        {
            Dungeon.getInstance().getHero().tryMove(Direction.SOUTH);

        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT)
        {
            Dungeon.getInstance().getHero().tryMove(Direction.WEST);

        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            Dungeon.getInstance().getHero().tryMove(Direction.EAST);
            
        }

    }

    @Override
    public void keyTyped(KeyEvent keyEvent)
    {
        // Auto-generated method stub

    }

}
