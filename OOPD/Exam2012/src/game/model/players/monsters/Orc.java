package game.model.players.monsters;

import game.model.Point;
import game.model.players.Monster;

public class Orc extends Monster
{
 
    protected static int unarmedDamage = 20;
    protected static int healthRegenerationRate = 10;

    public Orc(Point position)
    {
        super(position);
    }
    

   
}
