package puzzle.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import puzzle.model.BoardModel;

class Block extends JPanel
{
	private static final long serialVersionUID = 5889110138099806245L;
	
	private int value;
	
	public Block()
	{
		this(-1);
	}
	
	public Block(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	private int getImageX()
	{
		int rootOfSize = BoardModel.getCurrent().getNumberOfSlotsInARow();
		return (this.value % rootOfSize) * this.getWidth();
	}
	
	private int getImageY()
	{
		int rootOfSize = BoardModel.getCurrent().getNumberOfSlotsInARow();
		return (this.value / rootOfSize) * this.getHeight();
	}
	
	@Override
	public void paintComponent(Graphics graphics)
	{
		BufferedImage image = BoardModel.getCurrent().getBackgroundImage();
		graphics.drawImage(image, 0, 0, 50, 90, 0, 0, 250, 350, null);
		// TODO: by student
	}
}