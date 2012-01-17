package view.welcome;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EnterHeroName extends JPanel
{

    private static final long serialVersionUID = -2520800611587620385L;
    private JTextField inputHeroName;
    private JLabel infoText;
    
    public EnterHeroName(){
        this.infoText = new JLabel("Hero Name");
        this.infoText.setFont(new Font("Dialog", Font.PLAIN, 20));
        this.inputHeroName = new JTextField("",20);
        this.inputHeroName.setFont(new Font("Dialog", Font.PLAIN, 20));
        this.inputHeroName.requestFocus();
        
        add(this.infoText);
        add(this.inputHeroName);
    }

}
