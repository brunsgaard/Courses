package model.players.heroes;

import model.Point;
import model.players.Hero;

public class Warrior extends Hero

{
    protected static int damageMagnifier = 3;
    protected static int unarmedDamage = 5;
    protected static int healthRegenerationRate = 5;

    public Warrior(String name, Point position)
    {
        super(name, position);
    }

}
