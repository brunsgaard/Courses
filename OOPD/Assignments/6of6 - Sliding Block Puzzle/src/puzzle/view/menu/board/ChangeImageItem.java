package puzzle.view.menu.board;

import javax.swing.JMenuItem;

import puzzle.controller.menu.board.ChangeImage;
import puzzle.view.Language;



public class ChangeImageItem extends JMenuItem
{
	private static final long serialVersionUID = 6692923102032086022L;

	public ChangeImageItem()
	{
		super(Language.MENU_BOARD_CHANGE_IMAGE);
	}
}
