package puzzle.model.direction;

import static org.junit.Assert.*;

import puzzle.model.BoardModel;

import org.junit.Test;

import puzzle.model.direction.Down;
import puzzle.model.direction.Left;
import puzzle.model.direction.Right;
import puzzle.model.direction.Up;

public class DirectionTests
{
	@Test
	public void DownOppositeTest()
	{
		assertTrue(Down.getInstance().opposite() == Up.getInstance());
	}
	
	@Test
	public void UpOppositeTest()
	{
		assertTrue(Up.getInstance().opposite() == Down.getInstance());
	}
	
	@Test
	public void LeftOppositeTest()
	{		
		assertTrue(Left.getInstance().opposite() == Right.getInstance());
	}
	
	@Test
	public void RightOppositeTest()
	{
		assertTrue(Right.getInstance().opposite() == Left.getInstance());
	}
	
	@Test
	public void LeftGetNextPositionTest()
	{
		BoardModel boardModel = BoardModel.initialize();
		int currentPosition = boardModel.getPositionOfEmptySlot();
		int nextPosition = Left.getInstance().getNextPosition(boardModel);
		assertTrue(nextPosition == currentPosition - 1);
	}
	
	@Test
	public void UpGetNextPositionTest()
	{
		BoardModel boardModel = BoardModel.initialize();
		int currentPosition = boardModel.getPositionOfEmptySlot();
		int numberOfSlotsInARow = boardModel.getNumberOfSlotsInARow();
		int nextPosition = Up.getInstance().getNextPosition(boardModel);
		assertTrue(nextPosition == currentPosition - numberOfSlotsInARow);
	}
}