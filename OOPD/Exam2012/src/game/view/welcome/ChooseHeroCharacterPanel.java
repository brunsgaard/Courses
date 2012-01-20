package game.view.welcome;

import game.controller.welcome.EnterKeyListenerWP;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class ChooseHeroCharacterPanel extends JPanel
{
    private static final long serialVersionUID = -8531590967524511578L;

    private JPanel buttonPane;

    private JRadioButton warriorButton;
    private JRadioButton clericButton;
    private JRadioButton mageButton;

    private ButtonGroup groupOfHeroButtons;
    private String selectedHero;
    private JLabel HeroDescription;

    public ChooseHeroCharacterPanel()
    {

        setLayout(new GridLayout(1, 2));

        this.buttonPane = new JPanel();
        this.buttonPane.setLayout(new GridLayout(3, 1));
        this.HeroDescription = new JLabel();
        this.HeroDescription.setFont(new Font("Dialog", Font.BOLD, 15));

        this.warriorButton = new JRadioButton("Warrior");

        this.clericButton = new JRadioButton("Cleric");
        this.mageButton = new JRadioButton("Mage");

        this.groupOfHeroButtons = new ButtonGroup();

        this.groupOfHeroButtons.add(this.warriorButton);
        this.groupOfHeroButtons.add(this.clericButton);
        this.groupOfHeroButtons.add(this.mageButton);

        this.buttonPane.add(this.warriorButton);
        this.buttonPane.add(this.clericButton);
        this.buttonPane.add(this.mageButton);

        // Set default
        this.warriorButton.setSelected(true);
        this.setSelectedHero("Warrior");
        this.HeroDescription
                .setText("<html> Health Regeneration Rate: 5 <br /> Damage magnifier: 3 <html>");

        // add
        add(this.buttonPane);
        add(this.HeroDescription);

        // add actions
        radioButtonListener();
        clericButton.addKeyListener(new EnterKeyListenerWP());
        warriorButton.addKeyListener(new EnterKeyListenerWP());
        mageButton.addKeyListener(new EnterKeyListenerWP());

    }

    public String getSelectedHero()
    {
        return selectedHero;
    }

    public void setSelectedHero(String selectedHero)
    {
        this.selectedHero = selectedHero;
    }

    private void radioButtonListener()
    {

        this.warriorButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                HeroDescription
                        .setText("<html> Health Regeneration Rate: 5 <br /> Damage magnifier: 3 <html>");
                setSelectedHero("Warrior");

            }
        });

        this.clericButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                HeroDescription
                        .setText("<html> Health Regeneration Rate: 20 <br /> "
                                + "Damage magnifier: 1 <html>");
                setSelectedHero("Cleric");

            }
        });
        this.mageButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                HeroDescription
                        .setText("<html> Health Regeneration Rate: 10 <br /> "
                                + "Damage magnifier: 2 <html>");
                setSelectedHero("Mage");

            }
        });

    }

}
