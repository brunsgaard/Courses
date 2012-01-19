package game.view.dungeon;

import game.controller.Observer;
import game.controller.dungeon.TurnController;
import game.model.Dungeon;
import game.model.notification.INotification;
import game.model.notification.PlayerArmorChanged;
import game.model.notification.PlayerChangeWeapon;
import game.model.notification.PlayerHealthChanged;
import game.model.notification.TurnEnd;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatsPanel extends JPanel implements Observer<INotification>
{
    private JLabel health;
    private JLabel armor;
    private JLabel weapon;
    private JLabel turns;
    private int numTurns;
    
    public StatsPanel()
    {
        super();
        this.setLayout(new GridLayout(1,4));
        this.health = new JLabel("HP: " + Dungeon.getInstance().getHero().getHealth(),JLabel.CENTER);
        this.add(this.health);
        this.armor = new JLabel("Armor: 0",JLabel.CENTER);
        this.add(this.armor);
        this.weapon = new JLabel("Weapon: "+Dungeon.getInstance().getHero().getDamageLevel(),JLabel.CENTER);
        this.add(this.weapon);
        this.numTurns = 0;
        this.turns = new JLabel("Turns: "+numTurns,JLabel.CENTER);
        this.add(this.turns);
        Dungeon.getInstance().getHero().addObserver(this);
        TurnController.getInstance().addObserver(this);
    }
    
    public void update(PlayerHealthChanged change)
    {
        this.health.setText("HP: "+change.getHealth());
    }
    
    public void update(PlayerArmorChanged change)
    {
        this.armor.setText("Armor: "+change.getArmor());
    }
    
    public void update(PlayerChangeWeapon change)
    {
        this.weapon.setText("Weapon: "+Dungeon.getInstance().getHero().getDamageLevel());
    }
    
    public void update(TurnEnd change)
    {
        this.turns.setText("Turns: "+numTurns++);
    }
    
    public void update(INotification change)
    {
        if (change instanceof PlayerHealthChanged) this.update((PlayerHealthChanged) change);
        if (change instanceof PlayerArmorChanged) this.update((PlayerArmorChanged) change);
        if (change instanceof PlayerChangeWeapon) this.update((PlayerChangeWeapon) change);
        if (change instanceof TurnEnd) this.update((TurnEnd) change);
    }
}
