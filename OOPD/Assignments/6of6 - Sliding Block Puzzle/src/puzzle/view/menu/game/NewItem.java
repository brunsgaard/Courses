package puzzle.view.menu.game;

import javax.swing.JMenuItem;

import puzzle.controller.menu.game.New;
import puzzle.view.Language;



public class NewItem extends JMenuItem
{
	private static final long serialVersionUID = 2932579326301638538L;

	public NewItem()
	{
		super(Language.MENU_GAME_NEW);
		this.addActionListener(new puzzle.controller.menu.game.New());
	}
}
