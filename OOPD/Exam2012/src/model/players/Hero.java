package model.players;

import model.Point;
import model.items.Armor;
import model.items.Weapon;

public abstract class Hero extends Player

{
    protected String name;
    protected Weapon weapon;
    protected Armor armor;
    protected int damageMagnifier;

    public Hero(String name, Point position, Weapon weapon, Armor armor,
            int healthRegenerationRate, int damageMagnifier)
    {
        super(position, weapon.getDamage(), healthRegenerationRate);
        this.weapon = weapon;
        this.armor = armor;
        this.name = name;
        this.damageMagnifier = damageMagnifier;
    }
    
    @Override
    public int getDamageLevel()
    {
        return this.damage * this.damageMagnifier;
    }

    @Override
    public void takeDamage(int amount)
    {
        this.health -= Math.max(amount - this.armor.getResistence(),0);
        
    }
}
