package game.model.notification;

public class PlayerHealthChanged implements INotification
{
    private int newHealthValue;

    public PlayerHealthChanged(int health)
    {
        this.newHealthValue = health;
    }

    public int getHealth()
    {
        return this.newHealthValue;
    }
}
