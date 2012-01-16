package model.players.heroes;

import model.Point;
import model.items.Armor;
import model.items.Weapon;
import model.players.Hero;

public class Warrior extends Hero

{
    /**
     * Has the default values for Weapon, Armor, healthRegenerationRate,
     * damageMagnifier as described in the Exam assignment.
     */
    public Warrior(String name, Point position)
    {
        super(name, position, new Weapon(5), new Armor(0), 5, 3);       
    }

    /**
     * Overloaded constructor gives the ability to construct customized
     * Cleric instances if needed.
     */
    public Warrior(String name, Point position, Weapon weapon, Armor armor,
            int healthRegenerationRate, int damageMagnifier)
    {
        super(name, position, weapon, armor, healthRegenerationRate, damageMagnifier);
    }
}
