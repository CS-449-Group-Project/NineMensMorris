package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import Utils.TestCaseGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestFlyRule {
    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;

    @BeforeEach
    private void setup() {
        gameManager = new GameManager();
        board = new Board(gameManager);
        boardPane = new BoardPane(board, gameManager);
        board.reset();
    }

    @Test
    public void GameManager_GivenPlayerReachesThreePieces_FlyRuleImplemented() throws IOException
    {
        TestCaseGenerator testCaseObject = TestCaseGenerator.createFromFile("./test-inputs/flyRuleForWhite.td");

        assertEquals(testCaseObject.getExpectedGridSize(), Board.GRID_SIZE);

        for (CellPosition recordedPos: testCaseObject) {
            gameManager.performMove(board.getCell(recordedPos));
        }

        assertEquals(PlayerColor.WHITE, gameManager.getActivePlayer().getColor());
        assertEquals(Player.Phase.FLY_RULE, gameManager.getActivePlayer().getGamePhase());
    }

    @Test
    public void GameManager_GivenPlayerIsInFlyRulePhase_PlayerCanMovePieceAnywhere()
    {
        gameManager.getActivePlayer().setGamePhase(Player.Phase.FLY_RULE);

        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate66 = board.getCell(new CellPosition(6, 6));

        coordinate00.setState(CellState.BLACK);

        gameManager.performMove(coordinate00);
        gameManager.performMove(coordinate66);

        assertEquals(CellState.EMPTY, coordinate00.getCellState());
        assertEquals(CellState.BLACK, coordinate66.getCellState());
    }
}
