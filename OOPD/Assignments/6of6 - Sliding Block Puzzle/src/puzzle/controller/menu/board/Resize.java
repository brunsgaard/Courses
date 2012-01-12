package puzzle.controller.menu.board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import puzzle.model.BoardModel;
import puzzle.view.MainFrame;

public class Resize implements ActionListener {
    public Resize() {
	super();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
	Object[] possibilities = { new Integer(8), new Integer(15),
		new Integer(31) };
	Integer a = (Integer) JOptionPane
		.showInputDialog(MainFrame.getInstance(), "What size?",
			"Resize dialogue", JOptionPane.PLAIN_MESSAGE, null,
			possibilities, new Integer(15));
	
    BoardModel.getCurrent().resizeBoard(a.intValue());
    }
    
}
