package view.welcomePanel;

import javax.swing.JPanel;
import java.awt.GridLayout;

public class ChooseHeroCharacter extends JPanel
{
    private JPanel buttonPane;

    public ChooseHeroCharacter(){
        
        setLayout(new GridLayout(1, 2));
        
        this.buttonPane = new JPanel();
        this.buttonPane.setLayout(new GridLayout(3, 1));
        
        
    }

}
