package puzzle;
import puzzle.controller.BoardController;
import puzzle.model.BoardModel;
import puzzle.view.MainFrame;

public class Main
{
    public static void main(String[] args)
    {
        new Main();
    }

    private Main()
    {
        BoardModel boardModel = BoardModel.initialize();
        puzzle.view.Board boardView = puzzle.view.Board.initialize();

        boardModel.addObserver(boardView);

        boardView.addComponentListener(new BoardController());

        boardModel.restart();

        MainFrame.getInstance().setVisible(true);
    }
}