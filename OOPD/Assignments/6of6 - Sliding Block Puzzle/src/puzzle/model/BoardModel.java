package puzzle.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import puzzle.model.direction.Direction;
import puzzle.model.notification.INotification;
import puzzle.model.notification.MovedEmptySlot;
import puzzle.model.notification.Restarted;
import puzzle.model.notification.Winning;
import puzzle.model.position.Position;
import puzzle.model.position.PositionFactory;

import diku.oopd.Observable;
import diku.oopd.Observer;

public class BoardModel extends
	Observable<INotification, Observer<INotification>> {
    private static final int DEFAULT_NUMBER_OF_SLOTS_IN_A_ROW = 4;
    private static final String DEFAULT_PATH_TO_BACKGROUND_IMAGE = "./wrong.jpg";
    private static final int EMPTY_SLOT = 0;
    
    
    private static BoardModel instance;

    private int numberOfSlotsOnTheBoard;

    private int numberOfSlotsInARow;

    private ArrayList<Direction> moveHistory;
    
    

    /**
     * The board slots are numbers right to left, top to bottom. The integer 0
     * represents the empty slot.
     * 
     * Thus a solved 2x2 puzzle will be represented by an array with values
     * 
     * stateOfSlots[0] = 1 stateOfSlots[1] = 2 stateOfSlots[2] = 3
     * stateOfSlots[3] = 0
     */
    private int[] stateOfSlots;
    private int locationOfEmptySlot;

    private String pathToBackgroundImage;
    private BufferedImage backgroundImage;

    private BoardModel() {
	this.initializeSlotsWithNewNumberOfSlotsInARow(BoardModel.DEFAULT_NUMBER_OF_SLOTS_IN_A_ROW);
	this.pathToBackgroundImage = BoardModel.DEFAULT_PATH_TO_BACKGROUND_IMAGE;
	this.tryLoadBackgroundImage();
	this.moveHistory = new ArrayList<Direction>();
    }

    public static BoardModel initialize() {
	if (BoardModel.instance == null)
	    BoardModel.instance = new BoardModel();
	return BoardModel.instance;
    }

    public static BoardModel getCurrent() {
	return BoardModel.instance;
    }

    private void initializeSlotsWithNewNumberOfSlotsInARow(
	    int newNumberOfSlotsInARow) {
	this.numberOfSlotsInARow = newNumberOfSlotsInARow;
	this.numberOfSlotsOnTheBoard = newNumberOfSlotsInARow
		* newNumberOfSlotsInARow;
	this.stateOfSlots = new int[numberOfSlotsOnTheBoard];
    }

    private void initializeSlots() {
	for (int i = 0; i < (this.numberOfSlotsOnTheBoard - 1); i++) {
	    this.stateOfSlots[i] = i + 1;
	}

	this.locationOfEmptySlot = this.numberOfSlotsOnTheBoard - 1;
	this.stateOfSlots[locationOfEmptySlot] = BoardModel.EMPTY_SLOT;
    }

    public BufferedImage getBackgroundImage() {
	return this.backgroundImage;
    }

    public int getNumberOfSlotsInARow() {
	return this.numberOfSlotsInARow;
    }

    public int getNumberOfSlots() {
	return this.numberOfSlotsOnTheBoard;
    }

    public int getPositionOfEmptySlot() {
	return this.locationOfEmptySlot;
    }

    private void tryLoadBackgroundImage() {
	try {
	    this.backgroundImage = ImageIO.read(new File(
		    this.pathToBackgroundImage));
	} catch (Exception e) {
	    this.backgroundImage = null;
	}
    }

    public void changeBackgroundImage(String pathToNewImage) {
	this.pathToBackgroundImage = pathToNewImage;

	int currentWidthOfBackgroundImage = backgroundImage.getWidth();
	int currentHeightOfBackgroundImage = backgroundImage.getHeight();

	this.tryLoadBackgroundImage();
	this.scaleBackgroundImage(currentWidthOfBackgroundImage,
		currentHeightOfBackgroundImage);
	this.restart();
    }

    public void resizeBoard(int newSize) {
	int newNumberOfSlotsInARow = (int) Math.floor(Math.sqrt(++newSize));
	this.initializeSlotsWithNewNumberOfSlotsInARow(newNumberOfSlotsInARow);
	this.restart();
    }

    public void restart() {
	this.initializeSlots();
	this.notifyObservers(new Restarted());
	// this.performASequenceOfRandomMoves(); comment this in when you are
	// don whit the view part
    }

    public void scaleBackgroundImage(int newWidth, int newHeight) {
	try {
	    // TODO: by student
	    this.backgroundImage = null;
	} catch (Exception e) {
	    this.backgroundImage = null;
	}
    }

    public void performASequenceOfRandomMoves() {
	int numberOfMoves = this.getNumberOfSlots() * this.getNumberOfSlots();

	Direction choice;
	Direction[] choices;
	Position position;
	for (int i = 0; i < numberOfMoves; ++i) {
	    position = PositionFactory.positionOfEmptySlot(this);
	    choices = position.possibleMoves();
	    choice = choices[new Random().nextInt(choices.length)];

	    this.move(choice);
	}
    }

    public boolean move(Direction direction) {
	int newPosition = direction.getNextPosition(this);
	if (newPosition < 0)
	    return false;
	this.stateOfSlots[this.locationOfEmptySlot] = this.stateOfSlots[newPosition];
	this.stateOfSlots[newPosition] = BoardModel.EMPTY_SLOT;

	MovedEmptySlot moveEvent = new MovedEmptySlot(locationOfEmptySlot,
		newPosition);

	this.locationOfEmptySlot = newPosition;
	this.moveHistory.add(direction);

	this.notifyObservers(moveEvent);

	if (stateOfSlots[numberOfSlotsOnTheBoard - 1] == BoardModel.EMPTY_SLOT
		&& this.isInAWinningState())
	    this.notifyObservers(new Winning());

	return true;
    }

    public void undoAllMoves() {
	for (int i = this.numberOfSlotsOnTheBoard; i > 0; i-- ){
	    this.move(this.moveHistory.get(i-1).opposite());
	    this.moveHistory.clear();
	}
    }

    public boolean isInAWinningState() {
	for (int i = 0; i < (this.numberOfSlotsOnTheBoard - 1); i++) {
	    if (this.stateOfSlots[i] == i + 1)
		return false;
	}
	return true;
    }
}
