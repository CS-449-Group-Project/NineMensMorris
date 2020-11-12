package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class TestPiecePlacement {

    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;

    // format of test method names: public void Class_GivenScenario_Expectation()

    @BeforeEach
    private void setup() {
        gameManager = new GameManager();
        board = new Board(gameManager);
        boardPane = new BoardPane(board, gameManager);
        board.reset();
    }

    @Test
    public void Cell_EmptyCellIsClicked_CellStateIsBlack() {
        CellPosition position0 = new CellPosition(0, 0);
        CellPane coordinate00 = board.getCell(position0);

        gameManager.performMove(coordinate00, board);
        assertEquals(CellState.BLACK, coordinate00.getCellState());
    }

    @Test
    public void Cell_EmptyCellIsClickedAfterBlackMarbleIsPlaced_CellStateIsWhite() {
        CellPosition position0 = new CellPosition(0, 0);
        CellPane coordinate00 = board.getCell(position0);

        CellPosition position1 = new CellPosition(1, 1);
        CellPane coordinate11 = board.getCell(position1);

        gameManager.performMove(coordinate00, board);
        gameManager.performMove(coordinate11, board);

        assertEquals(CellState.WHITE, coordinate11.getCellState());
    }

    @Test
    public void Cell_EmptyCellIsClickedAfterAllMarblesArePlaced_NoPieceIsPlaced() {
        Player player = gameManager.getActivePlayer();

        while (player.hasMarblesInHand())
        {
            player.removeMarblesFromHand();
        }

        CellPosition position0 = new CellPosition(0, 0);
        CellPane coordinate00 = board.getCell(position0);

        gameManager.performMove(coordinate00, board);

        assertEquals(CellState.EMPTY, coordinate00.getCellState());
    }
}
