package view.welcome;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.GridLayout;

public class ChooseHeroCharacter extends JPanel
{
    private JPanel buttonPane;
    
    
    private JRadioButton warriorButton;
    private JRadioButton clericButton;
    private JRadioButton mageButton;
    
    private ButtonGroup  groupOfHeroButtons;
    private String selectedHero;  
    private JLabel HeroDescription;

    

    public ChooseHeroCharacter(){
        
        setLayout(new GridLayout(1, 2));
        
        this.buttonPane = new JPanel();
        this.buttonPane.setLayout(new GridLayout(3, 1));
        this.HeroDescription = new JLabel();
        
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

        //Set default
        this.warriorButton.setSelected(true);
        this.selectedHero = "Warrior";
        this.HeroDescription.setText("<html> Health Regeneration Rate: 5 <br /> Damage magnifier: 3 <html>");
        
        // add
        add(this.buttonPane);
        add(this.HeroDescription);
        
        
    }

}
