package model.players.heroes;

import model.Point;
import model.items.Armor;
import model.items.Weapon;
import model.players.Hero;

public class Cleric extends Hero

{
    /**
     * Has the default values for Weapon, Armor, healthRegenerationRate,
     * damageMagnifier as described in the Exam assignment.
     */

    public Cleric(String name, Point position)
    {
        super(name, position, new Weapon(5), new Armor(0), 20, 1);
    }

    /**
     * Overloaded constructor gives the ability to construct customized
     * Cleric instances if needed.
     */

    public Cleric(String name, Point position, Weapon weapon, Armor armor,
            int healthRegenerationRate, int damageMagnifier)
    {
        super(name, position, weapon, armor, healthRegenerationRate, damageMagnifier);
    }

}
