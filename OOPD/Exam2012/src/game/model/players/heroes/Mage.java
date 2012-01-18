package game.model.players.heroes;

import game.model.Point;
import game.model.players.Hero;

public class Mage extends Hero

{
    static
    {
        damageMagnifier = 2;
        unarmedDamage = 5;
        healthRegenerationRate = 10;
    }

    public Mage(String name, Point position)
    {
        super(name, position);
    }

}
