package model.players;

import model.Point;
import model.items.Armor;
import model.items.Weapon;

public abstract class Hero extends Player

{
    protected String name;
    protected Weapon weapon;
    protected Armor armor;
    protected static int damageMagnifier = 0;
    
    public Hero(String name, Point position)
    {
        super(position);
        this.weapon = null;
        this.armor = null;
        this.name = name;
    }

    @Override
    public int getDamageLevel()
    {
        int damage = this.weapon == null ? unarmedDamage : this.weapon
                .getDamage();
        return damage * damageMagnifier;
    }

    @Override
    public void takeDamage(int amount)
    {
        if (this.armor == null)
        {
            this.health -= amount;
            return;
        }
        int newArmorLevel = this.armor.getResistence() - amount;
        if (newArmorLevel < 0)
        {
            this.health += newArmorLevel;
            this.armor = null;
        } else
        {
            this.armor.setResistence(newArmorLevel);
        }
    }

    public void pickupItem(Weapon weapon)
    {
        int damage = this.weapon == null ? unarmedDamage : this.weapon
                .getDamage();
        if (weapon.getDamage() > damage)
            this.weapon = weapon;
    }

    public void pickupItem(Armor armor)
    {
        if (this.armor == null)
        {
            this.armor = armor;
            return;
        }
        this.armor.setResistence(this.armor.getResistence()
                + armor.getResistence());
    }
}
