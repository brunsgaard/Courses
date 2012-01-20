package game.view;

public final class Language
{
    private Language()
    {
        // Left blank to disallow instantiation of this class.
    }

    public static final String MAIN_FRAME_TITLE = "The Dungeon game";
    public static final String WELCOME_PANEL_BUTTON = "Weee.. let's go!!";
    public static final String WELCOME_PANEL_TITLE = "The DungeonGame";
    public static final String WELCOME_PANEL_CHECKBOX = "Load dungeon.map in home dir instead of included map";
    public static final String WELCOME_PANE_CLERIC = "Cleric";
    public static final String WELCOME_PANE_WARRIOR = "Warrior";
    public static final String WELCOME_PANE_MAGE = "Mage";
    public static final String WELCOME_PANE_CLERIC_STATS = "<html> Health Regeneration Rate: 20 <br /> "
            + "Damage magnifier: 1 <html>";
    public static final String WELCOME_PANE_WARRIOR_STATS = "<html> Health Regeneration Rate: 5 <br /> Damage magnifier: 3 <html>";
    public static final String WELCOME_PANE_MAGE_STATS = "<html> Health Regeneration Rate: 10 <br /> "
            + "Damage magnifier: 2 <html>";

    public static final String STAT_PANEL_HP = "HP: ";
    public static final String STAT_PANEL_ARMOR = "Armor: ";
    public static final String STAT_PANEL_WEAPON = "Weapon: ";
    public static final String STAT_PANEL_TURNS = "Turns: ";

    public static final String DUNGEON_PANEL_ROOM = "Room";
    public static final String DUNGEON_PANEL_MAP = "Map";
    public static final String DUNGEON_PANEL_INVENTORY = "Inventory";

    public static final String WELCOME_PANEL_GAME_DESCRIPTION = "In the game, the player guides a " +
    		"character who wanders a single-level dungeon accumulating treasure and killing monsters. " +
    		"Items can be picked up to increase armor or weapon. The game is turn-based, and after " +
    		"each turn both the Hero and the monsters regenerate their health. You control the hero on " +
    		"the Arrowkeys and find your Inventory on the i key. By default the hero are unarmed and must " +
    		"fight with his bare hands. The different Hero's has different strengths and weaknesses. " +
    		"So choose your Hero wisely. Good Luck!";
}