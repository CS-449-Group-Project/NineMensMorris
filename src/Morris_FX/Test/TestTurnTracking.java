package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class TestTurnTracking {

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
    public void Turn_BlackPlacesFirstMarble_TurnIsWhite() {
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));

        gameManager.performMove(coordinate00, board);

        assertEquals(PlayerColor.WHITE, gameManager.getCurrentPlayerColor());
    }

    @Test
    public void Turn_WhitePlacesSecondMarble_TurnIsBlack() {
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate11 = board.getCell(new CellPosition(1, 1));

        gameManager.performMove(coordinate00, board);
        gameManager.performMove(coordinate11, board);

        assertEquals(PlayerColor.BLACK, gameManager.getCurrentPlayerColor());
    }
}
