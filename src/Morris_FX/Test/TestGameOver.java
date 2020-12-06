package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane; // Okay to remove this import? -atp
import Utils.TestCaseGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameOver {
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
    public void GameManager_GivenPlayerReachesTwoPiecesOnBoard_GameIsOver() throws IOException
    {
        TestCaseGenerator testCaseObject = TestCaseGenerator.createFromFile("./test-inputs/blackWinsByPieceRemoval.td");

        assertEquals(testCaseObject.getExpectedGridSize(), Board.GRID_SIZE);

        for (CellPosition recordedPos: testCaseObject) {
            gameManager.performMove(board.getCell(recordedPos));
        }

        assertTrue(gameManager.isOver());
    }

    @Test
    public void GameManager_GivenPlayerHasNoValidMoves_GameIsOver() throws IOException
    {
        TestCaseGenerator testCaseObject = TestCaseGenerator.createFromFile("./test-inputs/whiteWinsByNoValidMovesRemaining.td");

        assertEquals(testCaseObject.getExpectedGridSize(), Board.GRID_SIZE);

        for (CellPosition recordedPos: testCaseObject) {
            gameManager.performMove(board.getCell(recordedPos));
        }

        assertTrue(gameManager.isOver());
    }



    @Test
    public void gameOverDoesNotOccurWithValidMovesLeft() throws IOException
    {
        TestCaseGenerator testCaseObject = TestCaseGenerator.createFromFile("./test-inputs/gameOverInMiddleOfGame.td");

        // make sure you are testing the same board
        assertEquals(testCaseObject.getExpectedGridSize(), Board.GRID_SIZE);

        for (CellPosition recordedPos: testCaseObject) {
            gameManager.performMove(board.getCell(recordedPos));
        }

        // check states here
        assertFalse(gameManager.isOver());
    }
}

