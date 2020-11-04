package Morris_FX.Test;

import Morris_FX.Logic.*;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.*;

//jUnit version 5.4+

public class TestGameBoardSetup {

    Board board;
    // Class_GivenScenario_Expectation
    @BeforeEach
    public void setup() {
        Turn turn = new Turn(PlayerColor.BLACK);
        GameState newGame = new GameState(turn);
        board = new Board(newGame);
        board.reset();
    }

    @Test
    public void Board_GivenNewGame_EmptyBoard() {
        // test 0,0
        // test 1,1
        // test 2,2
        setup();

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
    public void Board_GivenNewGame_TurnIsBlack() {}

    @Test
    public void Board_GivenGameReset_TurnIsBlack() {}

}
