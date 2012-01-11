package puzzle.view.menu.game;

import javax.swing.JMenu;

import puzzle.view.Language;


public class Menu extends JMenu
{
	private static final long serialVersionUID = -7662649391476732846L;

	public Menu()
	{
		super(Language.MENU_GAME);
		
		this.add(new NewItem());
		this.add(new SolveItem());
	}
}
