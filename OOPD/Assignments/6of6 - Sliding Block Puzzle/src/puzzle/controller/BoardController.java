package puzzle.controller;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import puzzle.model.BoardModel;

public class BoardController implements ComponentListener
{	
	public BoardController()
	{
		super();
	}
	
	@Override
	public void componentResized(ComponentEvent arg0)
	{
		puzzle.view.Board boardView = puzzle.view.Board.getCurrent();
		
		BoardModel.getCurrent().scaleBackgroundImage(boardView.getWidth(), boardView.getHeight());
		boardView.repaint();
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0)
	{
		// nothing should be done.
	}

	@Override
	public void componentMoved(ComponentEvent arg0)
	{
		// nothing should be done.
	}

	@Override
	public void componentShown(ComponentEvent arg0)
	{
		// nothing should be done.
	}
}