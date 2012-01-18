package game.model.players.heroes;

import game.model.Point;
import game.model.players.Hero;

public class Cleric extends Hero

{
    static
    {
        damageMagnifier = 1;
        unarmedDamage = 5;
        healthRegenerationRate = 20;
    }

    public Cleric(String name, Point position)
    {
        super(name, position);
    }

}
