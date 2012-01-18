package game.view;

import game.controller.dungeon.ArrowKeyListener;
import game.model.Dungeon;
import game.model.parser.DungeonParser;
import game.view.Language;
import game.view.dungeon.DungeonPanel;
import game.view.welcome.WelcomePanel;

import javax.swing.JFrame;

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
        // :TODO: Dungeon Description see OODP forum discussion in regard to
        // move description to map file and implement in parser
        Dungeon.getInstance()
                .setDescription(
                        "Fusce ultricies suscipit ipsum, quis euismod purus iaculis non."
                                + "Curabitur lectus mauris, aliquet vitae vestibulum eu, auctor eget purus."
                                + "Etiam facilisis massa et lacus pulvinar faucibus. Curabitur justo neque,"
                                + "imperdiet id tincidunt ac, iaculis eu leo. Ut arcu lacus, venenatis id"
                                + "mattis at, rutrum vitae massa. Integer nulla nulla, pretium non laoreet ut,"
                                + "consectetur a lectus. Nullam lobortis blandit mi ut suscipit."
                                + "Donec tempor rhoncus posuere. Suspendisse feugiat aliquam nunc eget placerat."
                                + " Integer ut odio in lorem placerat lobortis vitae ullamcorper arcu. "
                                + "Donec et pharetra magna +"
                                + "consectetur a lectus. Nullam lobortis blandit mi ut suscipit."
                                + "Donec tempor rhoncus posuere. Suspendisse feugiat aliquam nunc eget placerat."
                                + " Integer ut odio in lorem placerat lobortis vitae ullamcorper arcu. "
                                + "Donec et pharetra magna.");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set up dungeon
        // this.setExtendedState(MAXIMIZED_BOTH); // maximize window

        this.welcomePanel = new WelcomePanel();
        this.setContentPane(this.welcomePanel);

        this.pack();
        this.setVisible(true);
    }

    public void shiftToDungeonPanel()
    {
        this.welcomePanel.SendSelectedHeroToDungeon();
        this.welcomePanel.removeAll();
        this.dungeonPanel = new DungeonPanel();
        this.setContentPane(this.dungeonPanel);

        // this.pack();

        this.setVisible(true);
        this.dungeonPanel.grabFocus();
        this.dungeonPanel.addKeyListener(new ArrowKeyListener());
    }

    public static MainFrame getInstance()
    {
        if (MainFrame.instance == null)
            MainFrame.instance = new MainFrame();
        return MainFrame.instance;
    }

}
