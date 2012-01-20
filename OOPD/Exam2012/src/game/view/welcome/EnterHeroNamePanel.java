package game.view.welcome;

import game.controller.welcome.EnterKeyListenerWP;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EnterHeroNamePanel extends JPanel 
{

    private static final long serialVersionUID = -2520800611587620385L;
    private JTextField heroName;
    private JLabel infoText;

    public EnterHeroNamePanel()
    {
        this.infoText = new JLabel("Hero Name");
        this.infoText.setFont(new Font("Dialog", Font.PLAIN, 20));
        this.heroName = new JTextField("", 20);
        this.heroName.setFont(new Font("Dialog", Font.PLAIN, 20));
        this.heroName.requestFocus();

        this.heroName.addKeyListener(new EnterKeyListenerWP());

        add(this.infoText);
        add(this.heroName);

    }

    public String getHeroName()
    {
        return this.heroName.getText().trim();
    }

}
