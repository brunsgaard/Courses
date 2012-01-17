package view.welcome;

import view.Language;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Dungeon;
import model.Point;
import model.players.heroes.Cleric;
import model.players.heroes.Mage;
import model.players.heroes.Warrior;

public class WelcomePanel extends JPanel
{

    private static final long serialVersionUID = -8645219074328409909L;

    private JLabel title;
    private Description description;
    private EnterHeroName enterHeroName;
    private ChooseHeroCharacter chooseHeroCharacter;
    private JButton startButton;

    public WelcomePanel()
    {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // setBackground(Color.magenta);

        // Title (implement in Language)
        this.title = new JLabel(Language.WELCOME_PANEL_TITLE);
        title.setFont(new Font("Dialog", Font.ITALIC, 40));
        // this.title.setSize(400, 430);
        // this.title.setBackground(Color.cyan);
        // this.title.setOpaque(true);
        this.title.setAlignmentX(CENTER_ALIGNMENT);

        // Description

        this.description = new Description(Dungeon.getInstance()
                .getDescription());
        // this.description.setBackground(Color.DARK_GRAY);
        // EnterHeroName
        this.enterHeroName = new EnterHeroName();
        // this.enterHeroName.setBackground(Color.RED);

        // ChooseHeroCaracter
        this.chooseHeroCharacter = new ChooseHeroCharacter();
        // this.chooseHeroCharacter.setBackground(Color.YELLOW);

        // Start Button
        this.startButton = new JButton(Language.WELCOME_PANEL_BUTTON);
        this.startButton.setAlignmentX(CENTER_ALIGNMENT);

        // Put it all together
        this.draw();

    }

    // we have to be able to give the button to Mainframe, so
    // Mainframe can listen :)
    public JButton getStartButton()
    {
        return startButton;

    }

    private void draw()
    {
        add(this.title, BorderLayout.EAST);
        add(this.description, BorderLayout.CENTER);
        add(this.enterHeroName);
        add(this.chooseHeroCharacter);
        add(this.startButton);
    }

    public void SendSelectedHeroToDungeon()
    {
        String heroName = enterHeroName.getHeroName();
        String heroType = chooseHeroCharacter.getSelectedHero();
        Point startPoint = new Point(1, 1);

        if (heroType.equals("Warrior"))
        {
            Dungeon.getInstance().setHero(new Warrior(heroName, startPoint));
        } else if (heroType.equals("Cleric"))
        {
            Dungeon.getInstance().setHero(new Cleric(heroName, startPoint));
        } else if (heroType.equals("Mage"))
        {
            Dungeon.getInstance().setHero(new Mage(heroName, startPoint));

        }
    }

}
