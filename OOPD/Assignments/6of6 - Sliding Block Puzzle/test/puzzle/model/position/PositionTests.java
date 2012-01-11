package puzzle.model.position;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;


import org.junit.Test;

import puzzle.model.direction.Direction;
import puzzle.model.direction.Down;
import puzzle.model.direction.Left;
import puzzle.model.direction.Right;
import puzzle.model.direction.Up;
import puzzle.model.position.BottomEdge;
import puzzle.model.position.BottomLeftCorner;
import puzzle.model.position.BottomRightCorner;
import puzzle.model.position.LeftEdge;
import puzzle.model.position.Middle;
import puzzle.model.position.Position;
import puzzle.model.position.RightEdge;
import puzzle.model.position.TopEdge;
import puzzle.model.position.TopLeftCorner;
import puzzle.model.position.TopRightCorner;

public class PositionTests
{
	@Test
	public void bottomEdgeTest()
	{
		Direction[] expectedDirections = { Up.getInstance(), Left.getInstance(), Right.getInstance() };
		positionTest(BottomEdge.getInstance(), expectedDirections);
	}
	
	@Test
	public void bottomLeftCornerTest()
	{
		Direction[] expectedDirections = { Up.getInstance(), Right.getInstance() };
		positionTest(BottomLeftCorner.getInstance(), expectedDirections);
	}
	
	@Test
	public void bottomRightCornerTest()
	{
		Direction[] expectedDirections = { Up.getInstance(), Left.getInstance() };
		positionTest(BottomRightCorner.getInstance(), expectedDirections);
	}
	
	@Test
	public void rightEdgeTest()
	{
		Direction[] expectedDirections = { Down.getInstance(), Up.getInstance(), Left.getInstance() };
		positionTest(RightEdge.getInstance(), expectedDirections);
	}
	
	@Test
	public void leftEdgeTest()
	{
		Direction[] expectedDirections = { Down.getInstance(), Up.getInstance(), Right.getInstance() };
		positionTest(LeftEdge.getInstance(), expectedDirections);
	}
	
	@Test
	public void middleTest()
	{
		Direction[] expectedDirections = { Down.getInstance(), Up.getInstance(), Right.getInstance(), Left.getInstance() };
		positionTest(Middle.getInstance(), expectedDirections);
	}
	
	@Test
	public void topEdgeTest()
	{
		Direction[] expectedDirections = { Down.getInstance(), Right.getInstance(), Left.getInstance() };
		positionTest(TopEdge.getInstance(), expectedDirections);
	}
	
	@Test
	public void topLeftCornerTest()
	{
		Direction[] expectedDirections = { Down.getInstance(), Right.getInstance() };
		positionTest(TopLeftCorner.getInstance(), expectedDirections);
	}
	
	@Test
	public void topRightCornerTest()
	{
		Direction[] expectedDirections = { Down.getInstance(), Left.getInstance() };
		positionTest(TopRightCorner.getInstance(), expectedDirections);
	}
	
	private void positionTest(Position position, Direction[] expectedDirections)
	{
		List<Direction> possibleMoves = Arrays.asList(position.possibleMoves());
		assertTrue(possibleMoves.size() == expectedDirections.length);
		for (Direction direction : expectedDirections)
			assertTrue(possibleMoves.contains(direction));
	}
}