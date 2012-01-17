package view.welcome;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.MainFrame;

public class EnterHeroName extends JPanel
{

    private static final long serialVersionUID = -2520800611587620385L;
    private JTextField heroName;
    private JLabel infoText;

    public EnterHeroName()
    {
        this.infoText = new JLabel("Hero Name");
        this.infoText.setFont(new Font("Dialog", Font.PLAIN, 20));
        this.heroName = new JTextField("", 20);
        this.heroName.setFont(new Font("Dialog", Font.PLAIN, 20));
        this.heroName.requestFocus();

        add(this.infoText);
        add(this.heroName);
        heroName.addKeyListener(new KeyListener()
        {
            
            @Override
            public void keyTyped(KeyEvent e)
            {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void keyReleased(KeyEvent e)
            {
               if (e.getKeyCode() == KeyEvent.VK_ENTER) MainFrame.getInstance().shiftToDungeonPanel();
            }
            
            @Override
            public void keyPressed(KeyEvent e)
            {
                // TODO Auto-generated method stub
                
            }
        });
    }

    public String getHeroName()
    {
        return this.heroName.getText().trim();
    }

}
