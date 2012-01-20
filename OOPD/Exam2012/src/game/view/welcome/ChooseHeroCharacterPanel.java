package game.view.welcome;

import game.controller.welcome.EnterKeyListenerWP;
import game.model.players.heroes.Cleric;
import game.model.players.heroes.Mage;
import game.model.players.heroes.Warrior;
import game.view.Language;
import game.view.dungeon.TileLoader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ChooseHeroCharacterPanel extends JPanel
{
    private static final long serialVersionUID = -8531590967524511578L;

    private JPanel buttonPanel;

    private JRadioButton warriorButton;
    private JRadioButton clericButton;
    private JRadioButton mageButton;

    private ButtonGroup groupOfHeroButtons;
    private String selectedHero;
    private JLabel heroDescription;

    public ChooseHeroCharacterPanel()
    {
        super(new BorderLayout());
        this.heroDescription = new JLabel(Language.WELCOME_PANE_WARRIOR_STATS, JLabel.CENTER);
        this.heroDescription.setPreferredSize(new Dimension(1, 50));
        this.heroDescription.setFont(new Font("Dialog", Font.BOLD, 15));

        // Create a 2 by 3 grid for the icons and radio buttons
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new GridLayout(2, 3));
        
        this.warriorButton = new JRadioButton(Language.WELCOME_PANE_WARRIOR);
        this.clericButton = new JRadioButton(Language.WELCOME_PANE_CLERIC);
        this.mageButton = new JRadioButton(Language.WELCOME_PANE_MAGE);
        this.groupOfHeroButtons = new ButtonGroup();
        this.groupOfHeroButtons.add(this.warriorButton);
        this.groupOfHeroButtons.add(this.clericButton);
        this.groupOfHeroButtons.add(this.mageButton);

        this.buttonPanel.add(new JLabel(new ImageIcon(TileLoader
                .getTile(Warrior.class)), JLabel.CENTER));
        this.buttonPanel.add(new JLabel(new ImageIcon(TileLoader
                .getTile(Cleric.class)), JLabel.CENTER));
        this.buttonPanel.add(new JLabel(new ImageIcon(TileLoader
                .getTile(Mage.class)), JLabel.CENTER));

        // Create flowed panels for the buttons, for horizontal centering
        FlowLayout buttonFlow = new FlowLayout(FlowLayout.CENTER);
        JPanel panelWarriorButton = new JPanel(buttonFlow);
        panelWarriorButton.add(this.warriorButton);
        JPanel panelClericButton = new JPanel(buttonFlow);
        panelClericButton.add(this.clericButton);
        JPanel panelMageButton = new JPanel(buttonFlow);
        panelMageButton.add(this.mageButton);

        this.buttonPanel.add(panelWarriorButton);
        this.buttonPanel.add(panelClericButton);
        this.buttonPanel.add(panelMageButton);

        // Set default
        this.warriorButton.setSelected(true);
        this.setSelectedHero(Language.WELCOME_PANE_WARRIOR);

        // add
        add(this.buttonPanel, BorderLayout.CENTER);
        add(this.heroDescription, BorderLayout.PAGE_END);

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
                heroDescription.setText(Language.WELCOME_PANE_WARRIOR_STATS);
                setSelectedHero(Language.WELCOME_PANE_WARRIOR);

            }
        });

        this.clericButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                heroDescription.setText(Language.WELCOME_PANE_CLERIC_STATS);
                setSelectedHero(Language.WELCOME_PANE_CLERIC);

            }
        });
        this.mageButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                heroDescription.setText(Language.WELCOME_PANE_MAGE_STATS);
                setSelectedHero(Language.WELCOME_PANE_MAGE);

            }
        });

    }

}
