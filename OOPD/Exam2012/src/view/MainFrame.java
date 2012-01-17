package view;

import javax.swing.JFrame;
import parser.DungeonParser;

import model.Dungeon;
import model.Point;
import model.players.heroes.Mage;

import view.Language;
import view.dungeon.DungeonPanel;
import view.welcome.WelcomePanel;

public class MainFrame extends JFrame

{
    private static final long serialVersionUID = 5337190480729465776L;
    private static MainFrame instance;
    private DungeonParser dungeonParser;
    private WelcomePanel welcomePanel;
    private DungeonPanel dungeonPanel;

    private MainFrame()
    {
        super(Language.MAIN_FRAME_TITLE);
        this.dungeonParser = new DungeonParser();
        dungeonParser.parseMapFile();
        Dungeon.getInstance()
                .setDescription(
                        "Fusce ultricies suscipit ipsum, quis euismod purus iaculis non. Curabitur lectus mauris, aliquet vitae vestibulum eu, auctor eget purus. Etiam facilisis massa et lacus pulvinar faucibus. Curabitur justo neque, imperdiet id tincidunt ac, iaculis eu leo. Ut arcu lacus, venenatis id mattis at, rutrum vitae massa. Integer nulla nulla, pretium non laoreet ut, consectetur a lectus. Nullam lobortis blandit mi ut suscipit. Donec tempor rhoncus posuere. Suspendisse feugiat aliquam nunc eget placerat. Integer ut odio in lorem placerat lobortis vitae ullamcorper arcu. Donec et pharetra magna."); // :TODO
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up dungeon
        Dungeon.getInstance().setHero(new Mage("Eli", new Point(1, 1)));
        // this.setExtendedState(MAXIMIZED_BOTH); // maximize window

        this.dungeonPanel = new DungeonPanel();
        this.setContentPane(this.dungeonPanel);

        // this.welcomePanel = new WelcomePanel();
        // this.setContentPane(this.welcomePanel);

        this.pack();
        this.setVisible(true);

    }

    public static MainFrame getInstance()
    {
        if (MainFrame.instance == null)
            MainFrame.instance = new MainFrame();
        return MainFrame.instance;
    }
}
