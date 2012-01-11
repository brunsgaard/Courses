package puzzle.controller.menu.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import puzzle.model.BoardModel;

public class New implements ActionListener
{
	public New()
	{
		super();
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		BoardModel.getCurrent().restart();
	}
}
