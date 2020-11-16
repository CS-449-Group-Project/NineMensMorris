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
        player = gameManager.getActivePlayer();
        player.setGamePhase(Player.Phase.PIECE_MOVEMENT);
        boardPane = new BoardPane(board, gameManager);
        board.reset();
    }

    // Class_GivenScenario_Expectation()

    // test piece pick up method (hasPieceInHand)
    @Test
    public void Player_UserSelectsOwnPieceOnBoard_PieceIsPickedUp()
    {
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        coordinate00.setState(CellState.BLACK);

        gameManager.performMove(coordinate00);

        assertTrue(player.hasPieceToMove());
    }

    // test piece placement following piece pick up

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


}
