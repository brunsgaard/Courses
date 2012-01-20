package game.model.players;

import java.util.ArrayList;

import game.controller.dungeon.Direction;
import game.controller.notification.ChangeRoom;
import game.controller.notification.LootItem;
import game.controller.notification.PlayerArmorChanged;
import game.controller.notification.PlayerDied;
import game.controller.notification.PlayerHealthChanged;
import game.controller.notification.PlayerMoved;
import game.controller.notification.PlayerWeaponChanged;
import game.model.Dungeon;
import game.model.Point;
import game.model.items.Armor;
import game.model.items.Item;
import game.model.items.Weapon;

public abstract class Hero extends Player

{
    protected String name;
    protected Weapon weapon;
    protected Armor armor;
    protected int damageMagnifier;
    protected ArrayList<Item> Inventory;

    public Hero(String name, Point position, int unarmedDamage,
            int healthRegenerationRate, int damageMagnifier)
    {
        super(position, unarmedDamage, healthRegenerationRate);
        this.damageMagnifier = damageMagnifier;
        this.weapon = null;
        this.armor = null;
        this.name = name;
        this.Inventory = new ArrayList<>();
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
        {
            this.weapon = weapon;
            notifyObservers(new PlayerWeaponChanged(weapon));
        }
        
        this.Inventory.add(weapon);
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
        this.Inventory.add(armor);
    }

    public boolean tryMove(Direction direction)
    {

        Point endPosition = position.oneStep(direction);

        if (this.room.checkForDoor(endPosition)
                && !Dungeon.getInstance().isMonsterOnPosition(endPosition))
        {
            this.room.removePlayer(this);
            this.room = room.getDoors().get(endPosition);
            this.notifyObservers(new ChangeRoom(this.room));

        } else if (!this.room.isInside(endPosition))
        {
            return false;
        }

        Monster monster = Dungeon.getInstance().getMonsterFromPoint(
                endPosition);
        if (monster != null)
        {
            monster.takeDamage(this.getDamageLevel());
            return true;
        }

        Item item = this.room.loot(endPosition);
        if (item != null)
        {
            if (item instanceof Weapon)
                this.pickupItem((Weapon) item);
            if (item instanceof Armor)
                this.pickupItem((Armor) item);
            this.room.removeItem(item);
            this.notifyObservers(new LootItem(item));
        }
        
        this.position = endPosition;
        this.notifyObservers(new PlayerMoved(this.position));

        return true;
    }

    public ArrayList<Item> getInventory()
    {
        return Inventory;
    }
}
