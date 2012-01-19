package game.controller.notification;

public class PlayerArmorChanged implements INotification
{
    private int newArmorValue;

    public PlayerArmorChanged(int Armor)
    {
        this.newArmorValue = Armor;
    }

    public int getArmor()
    {
        return this.newArmorValue;
    }
}
