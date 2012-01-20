package game.view.welcome;


import game.model.Dungeon;
import game.model.Point;
import game.model.players.heroes.Cleric;
import game.model.players.heroes.Mage;
import game.model.players.heroes.Warrior;
import game.view.Language;
import game.view.MainFrame;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class WelcomePanel extends JPanel
{

    private static final long serialVersionUID = -8645219074328409909L;

    private JLabel title;
    private DescriptionPanel description;
    private EnterHeroNamePanel enterHeroName;
    private ChooseHeroCharacterPanel chooseHeroCharacter;
    private JButton startButton;

    public WelcomePanel()
    {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        

        this.title = new JLabel(Language.WELCOME_PANEL_TITLE);
        title.setFont(new Font("Dialog", Font.ITALIC, 40));
        this.title.setAlignmentX(CENTER_ALIGNMENT);


        this.description = new DescriptionPanel(Dungeon.getInstance()
                .getDescription());
        this.enterHeroName = new EnterHeroNamePanel();
        this.chooseHeroCharacter = new ChooseHeroCharacterPanel();
        this.startButton = new JButton(Language.WELCOME_PANEL_BUTTON);
        this.startButton.setAlignmentX(CENTER_ALIGNMENT);
        this.startButton.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                MainFrame.getInstance().shiftToDungeonPanel();
            }
        });

        this.draw();

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
        Point heroStartPosition = Dungeon.getInstance().getHeroStartPosition();

        if (heroType.equals(Language.WELCOME_PANE_WARRIOR))
        {
            Dungeon.getInstance().setHero(new Warrior(heroName, heroStartPosition));
        } else if (heroType.equals(Language.WELCOME_PANE_CLERIC))
        {
            Dungeon.getInstance().setHero(new Cleric(heroName, heroStartPosition));
        } else if (heroType.equals(Language.WELCOME_PANE_MAGE))
        {
            Dungeon.getInstance().setHero(new Mage(heroName, heroStartPosition));

        }
    }

}
