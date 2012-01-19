package game.model.players;

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

    public Hero(String name, Point position, int unarmedDamage,
            int healthRegenerationRate, int damageMagnifier)
    {
        super(position, unarmedDamage, healthRegenerationRate);
        this.damageMagnifier = damageMagnifier;
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

        // the end position of the move
        Point newPosition = position.oneStep(direction);

        // check for door else wall
        if (this.currentRoom.checkForDoor(newPosition)
                && !Dungeon.getInstance().isMonsterOnPosition(newPosition))
        {
            this.currentRoom.removePlayer(this);
            this.currentRoom = currentRoom.getDoors().get(newPosition);
            this.notifyObservers(new ChangeRoom(this.currentRoom));

        } else if (!this.currentRoom.isInside(newPosition))
        {
            return false;
        }

        // check for monster at end position
        Monster monster = Dungeon.getInstance().getMonsterFromNewPosition(
                newPosition);

        if (monster != null)
        {
            monster.takeDamage(this.getDamageLevel());
            // TODO Debug info
            System.out.println("Monsters Health: " + monster.getHealth());

            return true;
        }

        // check for items
        // TODO Do something with item
        Item item = this.currentRoom.loot(newPosition);

        if (item != null)
        {

            this.currentRoom.removeItem(item);
            this.notifyObservers(new LootItem(item));
            if (item instanceof Weapon)
                this.pickupItem((Weapon) item);
            if (item instanceof Armor)
                this.pickupItem((Armor) item);
        }

        // set position and notify observers..
        this.position = newPosition;
        this.notifyObservers(new PlayerMoved(this.position));

        return true;
    }
}
