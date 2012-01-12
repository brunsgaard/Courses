package puzzle.view.menu.game;

import javax.swing.JMenuItem;

import puzzle.controller.menu.game.Solve;
import puzzle.view.Language;



public class SolveItem extends JMenuItem
{
	private static final long serialVersionUID = 2932579326301638538L;

	public SolveItem()
	{
		super(Language.MENU_GAME_SOLVE);
	}
}
