package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameOver {
    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;
    private Player player;

    @BeforeEach
    private void setup() {
        gameManager = new GameManager();
        board = new Board(gameManager);
        boardPane = new BoardPane(board, gameManager);
        board.reset();
    }

    // Class_GivenScenario_Expectation()

    @Test
    public void GameManager_GivenPlayerReachesTwoPiecesOnBoard_GameIsOver() {
        // game over can only happen when no marbles are in hand
        while (gameManager.getActivePlayer().hasPiecesInHand()) {
            gameManager.getActivePlayer().removePiecesFromHand();
            gameManager.getInactivePlayer().removePiecesFromHand();
        }
        gameManager.getActivePlayer().setGamePhase(Player.Phase.PIECE_MOVEMENT);
        gameManager.getInactivePlayer().setGamePhase(Player.Phase.PIECE_MOVEMENT);

        for(int i = 0; i < 3;i++) {
            gameManager.getActivePlayer().increaseBoardPieces();
            gameManager.getInactivePlayer().increaseBoardPieces();
        }

        // black cells
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate03 = board.getCell(new CellPosition(0, 3));
        CellPane coordinate66 = board.getCell(new CellPosition(6, 6));

        // white cells
        CellPane coordinate22 = board.getCell(new CellPosition(2, 2));
        CellPane coordinate44 = board.getCell(new CellPosition(4, 4));
        CellPane coordinate55 = board.getCell(new CellPosition(5, 5));

        // cell which creates mill for black
        CellPane coordinate06 = board.getCell(new CellPosition(0, 6));

        coordinate00.setState(CellState.BLACK);
        coordinate03.setState(CellState.BLACK);
        coordinate66.setState(CellState.BLACK);

        coordinate22.setState(CellState.WHITE);
        coordinate44.setState(CellState.WHITE);
        coordinate55.setState(CellState.WHITE);

        gameManager.performMove(coordinate66); // black picks up piece
        gameManager.performMove(coordinate06); // black flies to this location to form mill
        gameManager.performMove(coordinate22); // black remove this white piece

        assertEquals(Player.Phase.GAME_OVER, gameManager.getActivePlayer().getGamePhase());
    }

    @Test
    public void GameManager_GivenPlayerHasNoValidMoves_GameIsOver()
    {
        // validMovesCounter behaves funny, so I had to simulate a full opener -atp

        // black cells (all corner cells)
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate60 = board.getCell(new CellPosition(6, 0));
        CellPane coordinate06 = board.getCell(new CellPosition(0, 6));
        CellPane coordinate66 = board.getCell(new CellPosition(6, 6));
        CellPane coordinate11 = board.getCell(new CellPosition(1, 1));
        CellPane coordinate15 = board.getCell(new CellPosition(1, 5));
        CellPane coordinate51 = board.getCell(new CellPosition(5, 1));
        CellPane coordinate55 = board.getCell(new CellPosition(5, 5));
        CellPane coordinate22 = board.getCell(new CellPosition(2, 2)); // piece to be removed

        // white cells (all middle cells)
        CellPane coordinate30 = board.getCell(new CellPosition(3, 0));
        CellPane coordinate03 = board.getCell(new CellPosition(0, 3));
        CellPane coordinate36 = board.getCell(new CellPosition(3, 6));
        CellPane coordinate63 = board.getCell(new CellPosition(6, 3));
        CellPane coordinate13 = board.getCell(new CellPosition(1, 3));
        CellPane coordinate31 = board.getCell(new CellPosition(3, 1));
        CellPane coordinate53 = board.getCell(new CellPosition(5, 3));
        CellPane coordinate35 = board.getCell(new CellPosition(3, 5));
        CellPane coordinate32 = board.getCell(new CellPosition(3, 2)); // white forms mill here (30, 31, 32)

        gameManager.performMove(coordinate00);
        gameManager.performMove(coordinate30);

        gameManager.performMove(coordinate60);
        gameManager.performMove(coordinate03);

        gameManager.performMove(coordinate06);
        gameManager.performMove(coordinate36);

        gameManager.performMove(coordinate66);
        gameManager.performMove(coordinate63);

        gameManager.performMove(coordinate11);
        gameManager.performMove(coordinate13);

        gameManager.performMove(coordinate15);
        gameManager.performMove(coordinate31);

        gameManager.performMove(coordinate51);
        gameManager.performMove(coordinate53);

        gameManager.performMove(coordinate55);
        gameManager.performMove(coordinate35);

        gameManager.performMove(coordinate22); // black's last move

        gameManager.performMove(coordinate32); // white forms mill
        gameManager.performMove(coordinate22); // white removes black's only movable piece

        assertEquals(Player.Phase.GAME_OVER, gameManager.getActivePlayer().getGamePhase());
    }
}

