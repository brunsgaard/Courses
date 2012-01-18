package game.model.players.heroes;

import game.model.Point;
import game.model.players.Hero;

public class Warrior extends Hero

{
    protected static final int damageMagnifier = 3;
    protected static final int unarmedDamage = 5;
    protected static final int healthRegenerationRate = 5;
    
    

    public Warrior(String name, Point position)
    {
        super(name, position);
        
    }

}
