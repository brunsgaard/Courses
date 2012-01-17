package model.players.monsters;

import model.Point;
import model.players.Monster;

public class Orc extends Monster
{
 
    protected static int unarmedDamage = 20;
    protected static int healthRegenerationRate = 10;

    public Orc(Point position)
    {
        super(position);
    }
    

   
}
