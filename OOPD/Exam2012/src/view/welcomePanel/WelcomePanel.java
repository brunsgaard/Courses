package view.welcomePanel;

import java.awt.Component;
import java.awt.Dimension;
import view.Language;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.corba.se.spi.ior.MakeImmutable;

import model.Dungeon;

public class WelcomePanel extends JPanel
{

    private static final long serialVersionUID = -8645219074328409909L;

    private JLabel title;
    private JLabel description;

    private EnterHeroName enterHeroName;
    private ChooseHeroCharacter chooseHeroCharacter;
    private JButton button;

    public WelcomePanel(Dungeon dungeon)
    {
       
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Title (implement in Language)
        this.title = new JLabel(Language.WELCOME_PANEL_TITLE);
        title.setFont(new Font("Dialog", Font.ITALIC, 50));

        // Description
        this.description = new JLabel("<html>" + dungeon.getDescription()
                + "<html>");
        this.descriptionLineBreaks();

        // EnterHeroName
        this.enterHeroName = new EnterHeroName();

        // ChooseHeroCaracter
        this.chooseHeroCharacter = new ChooseHeroCharacter();

        // Start Button
        this.button = new JButton(Language.WELCOME_PANEL_BUTTON);
        

        // Put it all together
        this.draw();

    }

    // TODO: Source
    // http://stackoverflow.com/questions/1842223/java-linebreaks-in-jlabels
    // can cound be improved with JtextArea/.
    public void descriptionLineBreaks()
    {
        int maxWidth = 900;
        Dimension size = this.description.getPreferredSize();
        if (size.width > maxWidth)
        {
            // Estimate the number of lines
            int lineCount = (int) Math.ceil(((double) size.width) / maxWidth);
            lineCount += 1; // Add one extra line as reserve
            size.width = maxWidth; // Apply the maximum width
            // Increase the height so that all lines will be visible
            size.height *= lineCount;
            this.description.setPreferredSize(size);
        }
    }

    public void draw()
    {
        removeAll();
        add(this.title);
        add(this.description);
        add(this.enterHeroName);
        add(this.chooseHeroCharacter);
        add(this.button);
    }

}
