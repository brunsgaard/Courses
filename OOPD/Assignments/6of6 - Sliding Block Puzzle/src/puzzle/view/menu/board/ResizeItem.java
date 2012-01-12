package puzzle.view.menu.board;

import javax.swing.JMenuItem;

import puzzle.controller.menu.board.Resize;
import puzzle.view.Language;



public class ResizeItem extends JMenuItem
{
	private static final long serialVersionUID = -5361510086677094305L;

	public ResizeItem()
	{
		super(Language.MENU_BOARD_RESIZE);
		this.addActionListener(new puzzle.controller.menu.board.Resize());
	}
}
