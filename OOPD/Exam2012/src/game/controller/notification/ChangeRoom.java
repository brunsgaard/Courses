//msn378
package game.controller.notification;

import game.model.Room;

public class ChangeRoom implements INotification
{
    private Room room;

    public ChangeRoom(Room room)
    {
        this.room = room;
    }

    public Room getRoom()
    {
        return this.room;
    }
}
