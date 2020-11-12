package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

//jUnit version 5.4+

public class TestGameBoardSetup {

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
    public void Board_GivenNewGame_EmptyCells() {
        CellPosition position0 = new CellPosition(0, 0);
        CellPane coordinate00 = board.getCell(position0);

        CellPosition position1 = new CellPosition(1, 1);
        CellPane coordinate11 = board.getCell(position1);

        CellPosition position2 = new CellPosition(2, 2);
        CellPane coordinate22 = board.getCell(position2);

        assertEquals(CellState.EMPTY, coordinate00.getCellState());
        assertEquals(CellState.EMPTY, coordinate11.getCellState());
        assertEquals(CellState.EMPTY, coordinate22.getCellState());
    }

    @Test
    public void Board_GivenNewGame_VoidCells() {
        CellPosition position3 = new CellPosition(3, 3);
        CellPane coordinate33 = board.getCell(position3);

        CellPosition position4 = new CellPosition(1, 2);
        CellPane coordinate12 = board.getCell(position4);

        assertEquals(CellState.VOID, coordinate33.getCellState());
        assertEquals(CellState.VOID, coordinate12.getCellState());
    }

    @Test
    public void Board_GivenNewGame_TurnIsBlack() {
        assertEquals(PlayerColor.BLACK, gameManager.getCurrentPlayerColor());
    }
}
