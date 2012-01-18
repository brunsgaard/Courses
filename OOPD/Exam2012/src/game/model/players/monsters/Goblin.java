package game.model.players.monsters;

import game.model.Point;
import game.model.players.Monster;

public class Goblin extends Monster
{
    static
    {
        unarmedDamage = 5;
        healthRegenerationRate = 2;
    }

    public Goblin(Point position)
    {
        super(position);

    }

}
