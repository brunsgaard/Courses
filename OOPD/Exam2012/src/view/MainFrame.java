package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import parser.DungeonParser;

import model.Dungeon;

import view.Language;
import view.welcomePanel.WelcomePanel;

public class MainFrame extends JFrame

{
 
    private static final long serialVersionUID = 5337190480729465776L;
    private static MainFrame instance;
    private DungeonParser dungeonParser;
    private Dungeon dungeon;
    private WelcomePanel welcomePanel;
    private JLabel label;
    private JPanel panel;
    
    private MainFrame(){
        super(Language.MAIN_FRAME_TITLE);
        this.dungeonParser = new DungeonParser(new Dungeon());
        this.dungeon = dungeonParser.parseMapFile();
        this.dungeon.setDescription("Fusce ultricies suscipit ipsum, quis euismod purus iaculis non. Curabitur lectus mauris, aliquet vitae vestibulum eu, auctor eget purus. Etiam facilisis massa et lacus pulvinar faucibus. Curabitur justo neque, imperdiet id tincidunt ac, iaculis eu leo. Ut arcu lacus, venenatis id mattis at, rutrum vitae massa. Integer nulla nulla, pretium non laoreet ut, consectetur a lectus. Nullam lobortis blandit mi ut suscipit. Donec tempor rhoncus posuere. Suspendisse feugiat aliquam nunc eget placerat. Integer ut odio in lorem placerat lobortis vitae ullamcorper arcu. Donec et pharetra magna."); // :TODO
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false); // TODO: if time implement resize)
        
        this.welcomePanel = new WelcomePanel(this.dungeon);
        add(welcomePanel);
        
        
        
        
        
        //this.pack();
        this.setVisible(true);
        
        
    }
    
    
    
    public static MainFrame getInstance() {
    if (MainFrame.instance == null)
        MainFrame.instance = new MainFrame();
    return MainFrame.instance;
    }
}
