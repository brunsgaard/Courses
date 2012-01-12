package puzzle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import puzzle.controller.ArrowKeyListener;
import puzzle.view.menu.MenuBar;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = -8808883923263763897L;

    private static MainFrame instance;

    private MainFrame() {
	super(Language.MAIN_FRAME_TITLE);

	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	Dimension minSize = new Dimension(400,400);
	this.setMinimumSize(minSize);
	
	puzzle.view.menu.MenuBar menu = new puzzle.view.menu.MenuBar();
	this.add(menu,BorderLayout.PAGE_START);
	
	//puzzle.view.Board.initialize();
	puzzle.view.Board board = puzzle.view.Board.getCurrent();

	if (board != null) {
	    //board.setBackground(new Color(0xFFFFFF));
	    this.add(board);
	    board.redraw();
	    
	}

	this.pack();

	this.bindKeyboard();
	this.setVisible(true);
    }

    public static MainFrame getInstance() {
	if (MainFrame.instance == null)
	    MainFrame.instance = new MainFrame();
	return MainFrame.instance;
    }

    private void bindKeyboard() {
	this.addKeyListener(new ArrowKeyListener());
    }
}
