package puzzle.controller.menu.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import puzzle.model.BoardModel;

public class Solve implements ActionListener
{
		public Solve()
	{
		super();
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		BoardModel.getCurrent().undoAllMoves();
	}
}
