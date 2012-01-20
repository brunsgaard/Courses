package game.view.dungeon;

import game.model.Dungeon;
import game.model.items.Item;
import game.model.items.Potion;
import game.model.items.Weapon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.RepaintManager;

public class InventoryActionListener implements ActionListener 
{
    private Item item;
    private InventoryPanel panel;
    
    public InventoryActionListener(Item item, InventoryPanel panel)
    {
        super();
        this.item = item;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (item instanceof Potion) {
            JButton button = (JButton) e.getSource();
            Dungeon.getInstance().getHero().drinkPotion((Potion) item);
            panel.remove(button);
            RepaintManager.currentManager(panel).markCompletelyDirty(panel);
        }
        if (item instanceof Weapon)
            Dungeon.getInstance().getHero().selectWeapon((Weapon) item);
    }
}
