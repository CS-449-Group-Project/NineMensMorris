package Morris_FX.Test;
import Morris_FX.Logic.*;
import Morris_FX.Morris;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestPiecePlacement {

    private Board board;
    private Turn turn;
    private GameState newGame;

    // format of test method names: public void Class_GivenScenario_Expectation()

    private void setup() {
        turn = new Turn(PlayerColor.BLACK);
        newGame = new GameState(turn);
        board = new Board(newGame);
        board.reset();
    }

    @Test
    public void Cell_EmptyCellIsClicked_CellStateIsBlack() {

        setup();
        CellPosition position0 = new CellPosition(0, 0);
        Cell coordinate00 = board.getCell(position0);

        board.performMove(position0);
        assertEquals(CellState.BLACK, coordinate00.getState());
    }

    @Test
    public void Cell_EmptyCellIsClickedAfterBlackMarbleIsPlaced_CellStateIsWhite() {

        setup();
        CellPosition position0 = new CellPosition(0, 0);

        CellPosition position1 = new CellPosition(1, 1);
        Cell coordinate11 = board.getCell(position1);

        board.performMove(position0);
        newGame.switchTurn();
        board.performMove(position1);

        assertEquals(CellState.WHITE, coordinate11.getState());
    }
}
