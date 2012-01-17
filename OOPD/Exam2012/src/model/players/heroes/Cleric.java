package model.players.heroes;

import model.Point;
import model.players.Hero;

public class Cleric extends Hero

{
    protected static int damageMagnifier = 1;
    protected static int unarmedDamage = 5;
    protected static int healthRegenerationRate = 20;

    public Cleric(String name, Point position)
    {
        super(name, position);
    }

}
