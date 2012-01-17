package model.players.monsters;

import model.Point;
import model.players.Monster;

public class Goblin extends Monster
{
    protected static int unarmedDamage = 5;
    protected static int healthRegenerationRate = 2;

    public Goblin(Point position)
    {
        super(position);
        
    }
    

}
