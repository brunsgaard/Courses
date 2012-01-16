package model.players.monsters;

import model.Point;
import model.players.Monster;

public class Orc extends Monster
{

    public Orc(Point position)
    {
        // parameters for superclass are
        // position, damage, healthRegenerationRate
        super(position, 20, 10);

    }

}
