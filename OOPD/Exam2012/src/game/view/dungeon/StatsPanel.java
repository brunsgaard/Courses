//msn378
package game.view.dungeon;

import game.controller.Observer;
import game.controller.dungeon.TurnController;
import game.controller.notification.INotification;
import game.controller.notification.PlayerArmorChanged;
import game.controller.notification.PlayerHealthChanged;
import game.controller.notification.PlayerWeaponChanged;
import game.controller.notification.TurnEnd;
import game.model.Dungeon;
import game.view.Language;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatsPanel extends JPanel implements Observer<INotification>
{
    /**
     * SerialVersionUID generated by Eclipse.
     */
    private static final long serialVersionUID = 1380702973300901745L;
    private JLabel health;
    private JLabel armor;
    private JLabel weapon;
    private JLabel turns;
    private int numTurns;

    public StatsPanel()
    {
        super();
        this.setLayout(new GridLayout(1, 4));
        this.health = new JLabel(Language.STAT_PANEL_HP
                + Dungeon.getInstance().getHero().getHealth(), JLabel.CENTER);
        this.add(this.health);
        this.armor = new JLabel(Language.STAT_PANEL_ARMOR + "0", JLabel.CENTER);
        this.add(this.armor);
        this.weapon = new JLabel(Language.STAT_PANEL_WEAPON
                + Dungeon.getInstance().getHero().getUnarmedDamage() + "x"
                + Dungeon.getInstance().getHero().getDamageMagnifier(),
                JLabel.CENTER);
        this.add(this.weapon);
        this.numTurns = 0;
        this.turns = new JLabel(Language.STAT_PANEL_TURNS + numTurns,
                JLabel.CENTER);
        this.add(this.turns);
        Dungeon.getInstance().getHero().addObserver(this);
        TurnController.getInstance().addObserver(this);
    }

    public void update(PlayerHealthChanged change)
    {
        this.health.setText(Language.STAT_PANEL_HP + change.getHealth());
    }

    public void update(PlayerArmorChanged change)
    {
        this.armor.setText(Language.STAT_PANEL_ARMOR + change.getArmor());
    }

    public void update(PlayerWeaponChanged change)
    {
        this.weapon.setText(Language.STAT_PANEL_WEAPON
                + change.getWeapon().getDamage() + "x"
                + Dungeon.getInstance().getHero().getDamageMagnifier());
    }

    public void update(TurnEnd change)
    {
        this.turns.setText(Language.STAT_PANEL_TURNS + ++numTurns);
    }

    public void update(INotification change)
    {
        if (change instanceof PlayerHealthChanged)
            this.update((PlayerHealthChanged) change);
        if (change instanceof PlayerArmorChanged)
            this.update((PlayerArmorChanged) change);
        if (change instanceof PlayerWeaponChanged)
            this.update((PlayerWeaponChanged) change);
        if (change instanceof TurnEnd)
            this.update((TurnEnd) change);
    }
}
