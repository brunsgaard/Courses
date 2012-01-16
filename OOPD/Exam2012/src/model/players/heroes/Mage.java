package model.players.heroes;

import model.Point;
import model.items.Armor;
import model.items.Weapon;
import model.players.Hero;

public class Mage extends Hero

{
    /**
     * Has the default values for Weapon, Armor, healthRegenerationRate,
     * damageMagnifier as described in the Exam assignment.
     */

    public Mage(String name, Point position)
    {
        super(name, position, new Weapon(5), new Armor(0), 10, 2);
    }

    /**
     * Overloaded constructor gives the ability to construct customized
     * Mage instances if needed.
     */

    
    public Mage(String name, Point position, Weapon weapon, Armor armor,
            int healthRegenerationRate, int damageMagnifier)
    {
        super(name, position, weapon, armor, healthRegenerationRate, damageMagnifier);
    }
}
