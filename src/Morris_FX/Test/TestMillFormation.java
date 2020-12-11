package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import Utils.TestCaseGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestMillFormation {

    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;

    @BeforeEach
    private void setup()
    {
        gameManager = GameManager.create();
        board = new Board(gameManager);
        boardPane = new BoardPane(board, gameManager);
        board.reset();
    }

    @Test
    public void GameManager_GivenAllPlayersHaveOnlyMillsFormed_AnOpponentsPieceCanBeRemoved() throws IOException
    {
      // test method should be updated -atp
      
        /*CellPosition[] positions = {
                new CellPosition(0,0),//black's
                new CellPosition(5,5),
                new CellPosition(0,3),
                new CellPosition(3,5),
                new CellPosition(0,6),
                new CellPosition(3,5),
                new CellPosition(3,5),
                new CellPosition(3,0),
                new CellPosition(1,3),
                new CellPosition(6,0),
                new CellPosition(1,3),
                new CellPosition(1,5),
        };
        for (CellPosition pos: positions) {
            gameManager.performMove(board.getCell(pos));
        }*/

        TestCaseGenerator testCaseObject = TestCaseGenerator.createFromFile("./test-inputs/allPiecesInMillsMimic.td");

        assertEquals(testCaseObject.getExpectedGridSize(), Board.GRID_SIZE);

        for (CellPosition recordedPos: testCaseObject) {
            gameManager.performMove(board.getCell(recordedPos));
        }

        assertTrue(gameManager.getPlayer().isMillFormed());

        /*assertEquals(PlayerColor.WHITE, gameManager.getCurrentPlayerColor());
        assertTrue(board.validateCellSelection(board.getCell(positions[0])));*/
    }

    @Test
    public void GameManager_GivenMillIsFormedDuringPlacementPhase_PlayerCanRemovePiece() throws IOException
    {

        TestCaseGenerator testCaseObject = TestCaseGenerator.createFromFile("./test-inputs/blackFormsMillDuringPlacementPhase.td");
        assertEquals(testCaseObject.getExpectedGridSize(), Board.GRID_SIZE);

        for (CellPosition recordedPos: testCaseObject) {
            gameManager.performMove(board.getCell(recordedPos));
        }

        assertTrue(gameManager.getPlayer().isMillFormed());
    }


    @Test
    public void GameManager_GivenMillIsFormedDuringMovementPhase_PlayerCanRemovePiece() throws IOException
    {
        TestCaseGenerator testCaseObject = TestCaseGenerator.createFromFile("./test-inputs/blackFormsMillDuringMovementPhase.td");

        assertEquals(testCaseObject.getExpectedGridSize(), Board.GRID_SIZE);

        for (CellPosition recordedPos: testCaseObject) {
            gameManager.performMove(board.getCell(recordedPos));
        }

        assertTrue(gameManager.getPlayer().isMillFormed());
    }

}
