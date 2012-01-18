package game.model.players;

import game.model.Point;
import game.model.items.Armor;
import game.model.items.Weapon;
import game.model.notification.PlayerArmorChanged;
import game.model.notification.PlayerDied;
import game.model.notification.PlayerHealthChanged;

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
        //this.unarmedDamage = -100;
        

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
            if (this.isDead())
                this.notifyObservers(new PlayerDied());
            this.notifyObservers(new PlayerHealthChanged(this.health));
            return;
        }
        int newArmorLevel = this.armor.getResistence() - amount;
        if (newArmorLevel < 0)
        {
            this.health += newArmorLevel;
            this.notifyObservers(new PlayerHealthChanged(this.health));
            if (this.isDead())
                this.notifyObservers(new PlayerDied());
            this.armor = null;
            this.notifyObservers(new PlayerArmorChanged(0));
        } else
        {
            this.armor.setResistence(newArmorLevel);
            this.notifyObservers(new PlayerArmorChanged(newArmorLevel));
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
