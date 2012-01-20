package game.view.dungeon;

import game.model.items.Item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryActionListener implements ActionListener 
{
    private Item item;
    public InventoryActionListener(Item item)
    {
        super();
        this.item = item;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("Hero select: "+this.item); //TODO
        
    }
}
