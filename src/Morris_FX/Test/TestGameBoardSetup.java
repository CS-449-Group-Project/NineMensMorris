package Morris_FX.Test;

import Morris_FX.Logic.Board;
import Morris_FX.Logic.GameState;
import Morris_FX.Logic.PlayerColor;
import Morris_FX.Logic.Turn;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
//jUnit version 5.4+

public class TestGameBoardSetup {

    // Class_GivenScenario_Expectation
    @BeforeEach
    public void setup() {
        Turn turn = new Turn(PlayerColor.BLACK);
        GameState newGame = new GameState(turn);
        Board board = new Board(newGame);
    }

    @Test
    public void Board_GivenNewGame_EmptyBoard() {
        // test 0,0
        // test 1,1
        // test 2,2

        int testArray[] = {1, 2, 3};

        for (int i : testArray)
        {

        }
    }

    @Test
    public void Board_GivenNewGame_TurnIsBlack() {}

    @Test
    public void Board_GivenGameReset_TurnIsBlack() {}

}
