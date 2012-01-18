package model.notification;

import model.Point;

public class PlayerMoved implements INotification
{
    private Point newPosition;

    public PlayerMoved(Point position)
    {
        this.newPosition = position;
    }

    public Point getPosition()
    {
        return this.newPosition;
    }
}