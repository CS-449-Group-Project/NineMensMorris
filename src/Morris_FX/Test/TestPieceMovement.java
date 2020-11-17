package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPieceMovement {
    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;
    private Player player;

    @BeforeEach
    private void setup() {
        gameManager = new GameManager();
        board = new Board(gameManager);
        player = gameManager.getCurrentPlayer();
        boardPane = new BoardPane(board, gameManager);
        board.reset();
        player.reset();
        player.setGamePhase(Player.Phase.PIECE_MOVEMENT);
    }

    // Class_GivenScenario_Expectation()

    @Test
    public void Player_UserSelectsOwnPieceOnBoard_PieceIsPickedUp()
    {
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        coordinate00.setState(CellState.BLACK);

        gameManager.performMove(coordinate00);

        assertTrue(player.hasPieceToMove());
    }

    @Test
    public void Player_UserSelectsEmptyCellAfterPickup_PieceIsPlaced()
    {
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        coordinate00.setState(CellState.BLACK);

        gameManager.performMove(coordinate00);

        CellPane coordinate30 = board.getCell(new CellPosition(3, 0));
        gameManager.performMove(coordinate30);

        assertEquals(CellState.EMPTY, coordinate00.getCellState());
        assertEquals(CellState.BLACK, coordinate30.getCellState());
    }


    @Test
    public void Player_UserSelectsNonAdjacentCellAfterPickup_PieceIsNotPlaced() {
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        coordinate00.setState(CellState.BLACK);

        board.validateCellSelection(coordinate00);
        gameManager.performMove(coordinate00);
        gameManager.addMoves(player.pieceToMove);
        gameManager.addPlacedPieceMoves(coordinate00);

        CellPane coordinate60 = board.getCell(new CellPosition(6, 0));

        if (board.validateCellSelection(coordinate60)) {
            gameManager.performMove(coordinate60);
        }

        assertEquals(CellState.BLACK, coordinate00.getCellState());
        assertEquals(CellState.EMPTY, coordinate60.getCellState());
    }
}
