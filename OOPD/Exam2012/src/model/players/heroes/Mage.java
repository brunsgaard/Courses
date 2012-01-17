package model.players.heroes;

import model.Point;
import model.players.Hero;

public class Mage extends Hero

{
    protected static int damageMagnifier = 2;
    protected static int unarmedDamage = 5;
    protected static int healthRegenerationRate = 10;

    public Mage(String name, Point position)
    {
        super(name, position);
    }

}
