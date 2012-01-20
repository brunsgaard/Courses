package game.model.players;

import game.controller.dungeon.Direction;
import game.controller.notification.ChangeRoom;
import game.controller.notification.LootItem;
import game.controller.notification.PlayerArmorChanged;
import game.controller.notification.PlayerDied;
import game.controller.notification.PlayerHealthChanged;
import game.controller.notification.PlayerMoved;
import game.controller.notification.PlayerWeaponChanged;
import game.model.Point;
import game.model.Room;
import game.model.items.Armor;
import game.model.items.Item;
import game.model.items.Potion;
import game.model.items.Weapon;

import java.util.ArrayList;

public abstract class Hero extends Player

{
    protected String name;
    protected Weapon weapon;
    protected Armor armor;
    protected int damageMagnifier;
    protected ArrayList<Item> inventory;

    public Hero(String name, Point position, int unarmedDamage,
            int healthRegenerationRate, int damageMagnifier)
    {
        super(position, unarmedDamage, healthRegenerationRate, 100);
        this.damageMagnifier = damageMagnifier;
        this.weapon = null;
        this.armor = null;
        this.name = name;
        this.inventory = new ArrayList<Item>();
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

    public void drinkPotion(Potion potion)
    {
        this.health += potion.getPoints();
        this.notifyObservers(new PlayerHealthChanged(this.health));
        this.inventory.remove(potion);
    }

    public void selectWeapon(Weapon weapon)
    {
        this.weapon = weapon;
        notifyObservers(new PlayerWeaponChanged(weapon));
    }

    public void pickupItem(Weapon weapon)
    {
        this.inventory.add(weapon);
        int damage = this.weapon == null ? unarmedDamage : this.weapon
                .getDamage();
        if (weapon.getDamage() > damage)
        {
            this.selectWeapon(weapon);
        }
    }

    public void pickupItem(Armor armor)
    {
        if (this.armor == null)
        {
            this.armor = armor;
        }
        this.armor.setResistence(this.armor.getResistence()
                + armor.getResistence());
        notifyObservers(new PlayerArmorChanged(this.armor.getResistence()));
    }

    public boolean tryMove(Direction direction)
    {

        Point endPosition = Point.oneStep(this.position, direction);

        if (this.room.checkForDoor(endPosition)
                && !Room.isMonsterOnPosition(room, endPosition))
        {
            this.room.removePlayer(this);
            this.room = room.getDoors().get(endPosition);
            this.notifyObservers(new ChangeRoom(this.room));
        } else if (!this.room.isInside(endPosition))
        {
            return false;
        }

        Monster monster = Room.getMonsterFromPoint(this.room, endPosition);
        if (monster != null)
        {
            monster.takeDamage(this.getDamageLevel());
            return true;
        }

        Item item = Room.loot(room, endPosition);
        if (item != null)
        {
            if (item instanceof Weapon)
                this.pickupItem((Weapon) item);
            if (item instanceof Armor)
                this.pickupItem((Armor) item);

            this.notifyObservers(new LootItem(item));
        }

        this.position = endPosition;
        this.notifyObservers(new PlayerMoved(this.position));

        return true;
    }

    public ArrayList<Item> getInventory()
    {
        return inventory;
    }
}
