package game.model.notification;

import game.model.items.Weapon;

public class PlayerChangeWeapon implements INotification
{
    private Weapon weapon;

    public PlayerChangeWeapon(Weapon weapon)
    {
        this.weapon = weapon;
    }

    public Weapon getWeapon()
    {
        return this.weapon;
    }

}
