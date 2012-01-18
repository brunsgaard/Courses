package game;
import game.view.MainFrame;

public class Main
{
    private Main()
    {

    }

    public static void main(String[] args)
    {
        MainFrame.getInstance().setVisible(true);
    }
}
