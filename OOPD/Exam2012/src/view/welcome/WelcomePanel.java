package view.welcome;

import view.Language;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.View;

import model.Dungeon;

public class WelcomePanel extends JPanel
{

    private static final long serialVersionUID = -8645219074328409909L;

    private JLabel title;
    private Description description;
    private EnterHeroName enterHeroName;
    private ChooseHeroCharacter chooseHeroCharacter;
    private JButton button;

    public WelcomePanel()
    {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
   
        
        
//        setBackground(Color.magenta);
        
        

        // Title (implement in Language)
        this.title = new JLabel(Language.WELCOME_PANEL_TITLE);
        title.setFont(new Font("Dialog", Font.ITALIC, 40));
//        this.title.setSize(400, 430);
//        this.title.setBackground(Color.cyan);
//        this.title.setOpaque(true);
        this.title.setAlignmentX(CENTER_ALIGNMENT);

        // Description
        
        this.description = new Description(Dungeon.getInstance().getDescription());
//        this.description.setBackground(Color.DARK_GRAY);
        // EnterHeroName
        this.enterHeroName = new EnterHeroName();
//        this.enterHeroName.setBackground(Color.RED);

        // ChooseHeroCaracter
        this.chooseHeroCharacter = new ChooseHeroCharacter();
//        this.chooseHeroCharacter.setBackground(Color.YELLOW);

        // Start Button
        this.button = new JButton(Language.WELCOME_PANEL_BUTTON);
        this.button.setAlignmentX(CENTER_ALIGNMENT);

        // Put it all together
        this.draw();
        

    }

    public void draw()
    {
        removeAll();
        add(this.title, BorderLayout.EAST);
        add(this.description,BorderLayout.CENTER);
        add(this.enterHeroName);
        add(this.chooseHeroCharacter);
        add(this.button);
    }

}
