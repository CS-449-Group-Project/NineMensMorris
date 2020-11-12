package Morris_FX.Test;

import Morris_FX.Logic.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class TestTurnTracking {

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
    public void Turn_BlackPlacesFirstMarble_TurnIsWhite() {
        CellPosition position0 = new CellPosition(0, 0);

        board.performMove(position0);
        newGame.switchTurn();

        assertEquals(PlayerColor.WHITE, newGame.getTurn().getPlayerColor());
    }

    @Test
    public void Turn_WhitePlacesSecondMarble_TurnIsBlack() {
        CellPosition position0 = new CellPosition(0, 0);
        CellPosition position1 = new CellPosition(1, 1);

        board.performMove(position0);
        newGame.switchTurn();
        board.performMove(position1);
        newGame.switchTurn();

        assertEquals(PlayerColor.BLACK, newGame.getTurn().getPlayerColor());
    }
}
