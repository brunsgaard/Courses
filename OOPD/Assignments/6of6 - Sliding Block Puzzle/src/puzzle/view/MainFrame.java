package puzzle.view;

import javax.swing.JFrame;

import puzzle.controller.ArrowKeyListener;
import puzzle.view.menu.MenuBar;


public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -8808883923263763897L;

	private static MainFrame instance;
	
	private MainFrame()
	{
		super(Language.MAIN_FRAME_TITLE);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// TODO: by student
		
		puzzle.view.Board board = puzzle.view.Board.getCurrent();
		
		if (board != null)
		{
		    this.add(board);
		}
		
		this.pack();
		
		this.bindKeyboard();
	}
	
	public static MainFrame getInstance()
	{
		if (MainFrame.instance == null)
			MainFrame.instance = new MainFrame();
		return MainFrame.instance;
	}
	
	private void bindKeyboard()
	{
		this.addKeyListener(new ArrowKeyListener());
	}
}
