package puzzle.controller.menu.board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import puzzle.model.BoardModel;
import puzzle.view.MainFrame;


public class Resize implements ActionListener
{
	public Resize()
	{
		super();
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
	JOptionPane jop = new JOptionPane();
	jop.showInputDialog(new JLabel("tafa"));
	
	}
}
