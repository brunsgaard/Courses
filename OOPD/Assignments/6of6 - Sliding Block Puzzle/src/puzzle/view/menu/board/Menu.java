package puzzle.view.menu.board;

import javax.swing.JMenu;

import puzzle.view.Language;


public class Menu extends JMenu
{
	private static final long serialVersionUID = 1408992618601053141L;

	public Menu()
	{
		super(Language.MENU_BOARD);
		
		this.add(new ResizeItem());
		this.add(new ChangeImageItem());
	}
}
