package Morris_FX.Test;

import Morris_FX.Logic.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

//jUnit version 5.4+

public class TestGameBoardSetup {

    private Board board;
    private Turn turn;
    private GameState newGame;
    // format of test method names: public void Class_GivenScenario_Expectation()

    @BeforeEach
    private void setup() {
        turn = new Turn(PlayerColor.BLACK);
        newGame = new GameState(turn);
        board = new Board(newGame);
        board.reset();
    }

    @Test
    public void Board_GivenNewGame_EmptyCells() {
        CellPosition position0 = new CellPosition(0, 0);
        Cell coordinate00 = board.getCell(position0);

        CellPosition position1 = new CellPosition(1, 1);
        Cell coordinate11 = board.getCell(position1);

        CellPosition position2 = new CellPosition(2, 2);
        Cell coordinate22 = board.getCell(position2);

        assertEquals(CellState.EMPTY, coordinate00.getState());
        assertEquals(CellState.EMPTY, coordinate11.getState());
        assertEquals(CellState.EMPTY, coordinate22.getState());
    }

    @Test
    public void Board_GivenNewGame_VoidCells() {
        CellPosition position3 = new CellPosition(3, 3);
        Cell coordinate33 = board.getCell(position3);

        CellPosition position4 = new CellPosition(1, 2);
        Cell coordinate12 = board.getCell(position4);

        assertEquals(CellState.VOID, coordinate33.getState());
        assertEquals(CellState.VOID, coordinate12.getState());
    }

    @Test
    public void Board_GivenNewGame_TurnIsBlack() {
        assertEquals(PlayerColor.BLACK, newGame.getTurn().getPlayerColor());
    }
}
