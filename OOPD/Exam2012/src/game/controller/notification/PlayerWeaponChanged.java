package game.controller.notification;

import game.model.items.Weapon;

public class PlayerWeaponChanged implements INotification
{
    private Weapon weapon;

    public PlayerWeaponChanged(Weapon weapon)
    {
        this.weapon = weapon;
    }

    public Weapon getWeapon()
    {
        return this.weapon;
    }

}
