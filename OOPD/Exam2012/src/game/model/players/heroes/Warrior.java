package game.model.players.heroes;

import game.model.Point;
import game.model.players.Hero;

public class Warrior extends Hero

{
    static
    {
        damageMagnifier = 3;
        unarmedDamage = 5;
        healthRegenerationRate = 5;
    }

    public Warrior(String name, Point position)
    {
        super(name, position);

    }

}
