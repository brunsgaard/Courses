package game.model.players.monsters;

import game.model.Point;
import game.model.players.Monster;

public class Orc extends Monster
{

    static
    {
        unarmedDamage = 20;
        healthRegenerationRate = 10;
    }

    public Orc(Point position)
    {
        super(position);
    }

}
