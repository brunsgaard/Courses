package puzzle.view;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;

import puzzle.model.BoardModel;
import puzzle.model.notification.MovedEmptySlot;
import puzzle.model.notification.INotification;
import puzzle.model.notification.Restarted;

import diku.oopd.Observer;

public class Board extends JPanel implements Observer<INotification> {
    private static final long serialVersionUID = 5689846694617785525L;

    private static Board instance;

    private Block activeBlock;

    private Board() {
	super();
	this.activeBlock = new Block(BoardModel.EMPTY_SLOT);
	this.redraw();
    }

    public static Board getCurrent() {
	return Board.instance;
    }

    public static Board initialize() {
	if (Board.instance == null)
	    Board.instance = new Board();
	return Board.instance;
    }

    public void redraw() {
	this.removeAll();

	int rootOfSize = BoardModel.getCurrent().getNumberOfSlotsInARow();
	this.setLayout(new GridLayout(rootOfSize, rootOfSize));
	this.addAllBlocks();
    }

    private void addAllBlocks() {
	this.addInactiveBlocks();
	this.add(this.activeBlock, BoardModel.getCurrent());
    }

    private void addInactiveBlocks() {
	for (int i = 0; i < BoardModel.getCurrent().getNumberOfSlots(); i++) {
	    int slot = BoardModel.getCurrent().getSlot(i);
	    if (slot != BoardModel.EMPTY_SLOT) {
		Block b = new Block();
		this.add(b);
	    }
	}
    }

    public void update(MovedEmptySlot movement) {
	Component inactiveBlock = this.getComponent(movement.getEnd());

	this.add(this.activeBlock, movement.getEnd());
	this.add(inactiveBlock, movement.getStart());

	this.doLayout();
    }

    public void update(Restarted restart) {
	this.redraw();
	this.doLayout();
    }

    public void update(INotification change) {
	if (change instanceof MovedEmptySlot)
	    this.update((MovedEmptySlot) change);
	if (change instanceof Restarted)
	    this.update((Restarted) change);
    }
}