package game.controller.dungeon;

import game.controller.Observable;
import game.controller.Observer;
import game.controller.notification.INotification;
import game.controller.notification.TurnEnd;
import game.controller.notification.TurnStart;

public class TurnController extends
        Observable<INotification, Observer<INotification>>
{
    private static TurnController instance;

    private TurnController()
    {

    }

    public static TurnController getInstance()
    {
        if (TurnController.instance == null)
            TurnController.instance = new TurnController();
        return TurnController.instance;
    }

    public void doTurn()
    {
        this.notifyObservers(new TurnStart());
        this.notifyObservers(new TurnEnd());
    }

}
