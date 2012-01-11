package puzzle.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import puzzle.model.BoardModel;
import puzzle.model.direction.Down;
import puzzle.model.direction.Left;
import puzzle.model.direction.Right;
import puzzle.model.direction.Up;


public class ArrowKeyListener implements KeyListener
{
	@Override
	public void keyReleased(KeyEvent keyEvent)
	{
		BoardModel boardModel = BoardModel.getCurrent();
		boardModel.move(Up.getInstance());
		// TODO: by student
	}
	
	@Override
	public void keyPressed(KeyEvent keyEvent)
	{
		// nothing to do.
	}

	@Override
	public void keyTyped(KeyEvent keyEvent)
	{
		// nothing to do.
	}
}
