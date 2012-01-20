//msn378
package game.controller.welcome;

import game.view.MainFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EnterKeyListenerWP implements KeyListener
{

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        // Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) MainFrame.getInstance().shiftToDungeonPanel();
        
    }

    @Override
    public void keyTyped(KeyEvent keyEvent)
    {
        // Auto-generated method stub
        
    }

}
