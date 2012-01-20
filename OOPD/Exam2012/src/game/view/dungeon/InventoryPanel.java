//msn378
package game.view.dungeon;

import game.controller.Observer;
import game.controller.dungeon.InventoryActionListener;
import game.controller.notification.INotification;
import game.controller.notification.LootItem;
import game.model.Dungeon;
import game.model.items.Armor;
import game.model.items.Item;
import game.model.items.Potion;
import game.model.items.Weapon;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class InventoryPanel extends JPanel implements Observer<INotification>
{
    private static final long serialVersionUID = -4733068797192419803L;

    public InventoryPanel()
    {
        super(new FlowLayout());
        Dungeon.getInstance().getHero().addObserver(this);
    }

    public void update(LootItem change)
    {
        if (change.getLoot() instanceof Armor) return;
        Item item = change.getLoot();
        BufferedImage tile = new BufferedImage(TileLoader.tilePixelSize, TileLoader.tilePixelSize, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D gfx = tile.createGraphics();
        gfx.drawImage(TileLoader.getTile(item), 0, 0, null);
        gfx.setColor(Color.BLACK);
        gfx.fillRect(1, 1, 16, 12);
        gfx.setColor(Color.WHITE);
        gfx.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
        if (item instanceof Weapon) {
            gfx.drawString("" + ((Weapon) item).getDamage(), 2, 10);
        } else if (item instanceof Potion) {
            gfx.drawString("" + ((Potion) item).getPoints(), 2, 10);
        }
        gfx.dispose();
        ImageIcon icon = new ImageIcon(tile);
        JButton button = new JButton(icon);
        button.setFocusable(false);
        button.addActionListener(new InventoryActionListener(item,this));
        this.add(button);
    }

    @Override
    public void update(INotification change)
    {
        if (change instanceof LootItem)
            this.update((LootItem) change);
    }
}
