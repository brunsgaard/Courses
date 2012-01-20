//msn378
package game.controller.notification;

import game.model.items.Item;

public class LootItem implements INotification
{
    private Item loot;

    public LootItem(Item loot)
    {
        this.loot = loot;
    }

    public Item getLoot()

    {
        return this.loot;
    }
}
