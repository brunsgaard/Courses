//msn378
package game.view;

import game.controller.Observer;
import game.controller.dungeon.ArrowKeyListener;
import game.controller.dungeon.TabKeyListener;
import game.controller.notification.INotification;
import game.controller.notification.PlayerDied;
import game.model.Dungeon;
import game.model.parser.DungeonParser;
import game.view.dungeon.DungeonPanel;
import game.view.welcome.WelcomePanel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame implements Observer<INotification>

{
    private static final long serialVersionUID = 5337190480729465776L;
    private static MainFrame instance;
    private DungeonParser dungeonParser;
    private WelcomePanel welcomePanel;
    private DungeonPanel dungeonPanel;
    private boolean godmode;

    private MainFrame()
    {
        super(Language.MAIN_FRAME_TITLE);
        this.dungeonParser = new DungeonParser();
        Dungeon.getInstance().setDescription(
                Language.WELCOME_PANEL_GAME_DESCRIPTION);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.welcomePanel = new WelcomePanel();
        this.setContentPane(this.welcomePanel);

        this.pack();
        this.setVisible(true);
        this.godmode = false;
    }

    public void shiftToDungeonPanel()
    {
        this.parseDungeon();
        this.welcomePanel.sendSelectedHeroToDungeon();
        this.welcomePanel.removeAll();
        this.dungeonPanel = new DungeonPanel();
        this.setContentPane(this.dungeonPanel);

        this.setVisible(true);
        this.dungeonPanel.grabFocus();
        this.dungeonPanel
                .addKeyListener(new ArrowKeyListener(this.dungeonPanel));
        this.dungeonPanel.addKeyListener(new TabKeyListener(this.dungeonPanel));
        Dungeon.getInstance().getHero().addObserver(this);
    }

    private void parseDungeon()
    {
        try
        {
            if (this.welcomePanel.loadFromHomeDir())
            {
                dungeonParser.parseMapFile(new FileInputStream(new File(System
                        .getProperty("user.home") + "/dungeon.map")));
            } else
            {
                dungeonParser.parseMapFile(DungeonParser.class.getResource(
                        "dungeon.map").openStream());
            }

        } catch (FileNotFoundException e)
        {
            System.out.println("Woops.. something went wrong while"
                    + " loading dungeon.map, please place a "
                    + "valid dungeon.map file in your homedir ");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        MainFrame.getInstance().setVisible(true);
    }

    public static MainFrame getInstance()
    {
        if (MainFrame.instance == null)
            MainFrame.instance = new MainFrame();
        return MainFrame.instance;
    }
    
    public void update(PlayerDied change)
    {
        if (this.godmode) return;
        int n = JOptionPane.showConfirmDialog(
                this,
                "GAME OVER!\nWould you like to continue in god mode?",
                "Game over",
                JOptionPane.YES_NO_OPTION);
        if (n == 1) {
            System.exit(0);
        } else {
            this.godmode = true;            
        }
    }

    @Override
    public void update(INotification change)
    {
        if (change instanceof PlayerDied) this.update((PlayerDied) change);
    }

}
