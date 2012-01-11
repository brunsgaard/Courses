package puzzle.view.menu;

import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar
{
	private static final long serialVersionUID = 4122982795116481961L;

	public MenuBar()
	{
		super();
		
		this.add(new puzzle.view.menu.game.Menu());
		this.add(new puzzle.view.menu.board.Menu());
	}
}
