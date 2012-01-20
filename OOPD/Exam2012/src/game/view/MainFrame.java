//msn378
package game.view;

import game.controller.dungeon.ArrowKeyListener;
import game.controller.dungeon.TabKeyListener;
import game.model.Dungeon;
import game.model.parser.DungeonParser;
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
        Dungeon.getInstance().setDescription(
                Language.WELCOME_PANEL_GAME_DESCRIPTION);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        this.dungeonPanel.addKeyListener(new ArrowKeyListener(this.dungeonPanel));
        this.dungeonPanel.addKeyListener(new TabKeyListener(this.dungeonPanel));
    }
    
    public void reInitialize(){
        MainFrame.instance = new MainFrame();
    }

    public static MainFrame getInstance()
    {
        if (MainFrame.instance == null)
            MainFrame.instance = new MainFrame();
        return MainFrame.instance;
    }

}
