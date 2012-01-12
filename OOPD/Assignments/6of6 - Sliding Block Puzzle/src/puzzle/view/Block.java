package puzzle.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import puzzle.model.BoardModel;

class Block extends JPanel {
    private static final long serialVersionUID = 5889110138099806245L;

    private int value;

    public Block() {
	this(BoardModel.EMPTY_SLOT);
    }

    public Block(int value) {
	this.value = value;
    }

    public int getValue() {
	return value;
    }

    private int getImageX() {
	int rootOfSize = BoardModel.getCurrent().getNumberOfSlotsInARow();
	return (this.value % rootOfSize) * this.getWidth();
    }

    private int getImageY() {
	int rootOfSize = BoardModel.getCurrent().getNumberOfSlotsInARow();
	return (this.value / rootOfSize) * this.getHeight();
    }

    @Override
    public void paintComponent(Graphics graphics) {
	if (this.getValue() == BoardModel.EMPTY_SLOT) {
	    this.setBackground(new Color(0xFFFFFF));
	    return;
	}
	BufferedImage image = BoardModel.getCurrent().getBackgroundImage();
	graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),
		this.getImageX(), this.getImageY(),
		this.getImageX() + this.getWidth(),
		this.getImageY() + this.getHeight(), null);
    }
}