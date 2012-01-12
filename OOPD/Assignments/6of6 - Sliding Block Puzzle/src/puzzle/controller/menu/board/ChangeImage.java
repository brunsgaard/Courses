package puzzle.controller.menu.board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import puzzle.model.BoardModel;
import puzzle.model.direction.Up;
import puzzle.view.MainFrame;

public class ChangeImage implements ActionListener {
    public ChangeImage() {
	super();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	JFileChooser chooser = new JFileChooser();
	// chooser.setDialogType(JFileChooser.OPEN_DIALOG);
	if (chooser.showOpenDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
	    File f = chooser.getSelectedFile();
	    BoardModel.getCurrent().changeBackgroundImage(f.getPath());
	}
    }
}
