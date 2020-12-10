package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFlyRule {
    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;
    private Player player;

    @BeforeEach
    private void setup() {
        gameManager = GameManager.create();
        board = new Board(gameManager);
        player = gameManager.getPlayer();
        boardPane = new BoardPane(board, gameManager);
        board.reset();
        gameManager.resetGameManager();

        // piece movement happens when no pieces in hand
        while (gameManager.getPlayer().hasPiecesInHand()) {
            gameManager.getPlayer().removePiecesFromHand();
            gameManager.getOpponent().removePiecesFromHand();
        }
    }

    @Test
    public void GameManager_GivenPlayerReachesThreePieces_FlyRuleImplemented()
    {
        gameManager.getPlayer().setGamePhase(Player.Phase.PIECE_MOVEMENT);
        gameManager.getOpponent().setGamePhase(Player.Phase.PIECE_MOVEMENT);
        // black cells
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate30 = board.getCell(new CellPosition(3, 0));
        CellPane coordinate03 = board.getCell(new CellPosition(0, 3));
        CellPane coordinate36 = board.getCell(new CellPosition(3, 6));

        // white cells
        CellPane coordinate11 = board.getCell(new CellPosition(1, 1));
        CellPane coordinate22 = board.getCell(new CellPosition(2, 2));
        CellPane coordinate44 = board.getCell(new CellPosition(4, 4));
        CellPane coordinate55 = board.getCell(new CellPosition(5, 5));

        // black moves here to form mill
        CellPane coordinate06 = board.getCell(new CellPosition(0, 6));

        coordinate00.setState(CellState.BLACK);
        coordinate30.setState(CellState.BLACK);
        coordinate03.setState(CellState.BLACK);
        coordinate36.setState(CellState.BLACK);

        coordinate11.setState(CellState.WHITE);
        coordinate22.setState(CellState.WHITE);
        coordinate44.setState(CellState.WHITE);
        coordinate55.setState(CellState.WHITE);

        // hard code board piece population

        while(gameManager.getPlayer().getBoardPieces() < 4)
        {
            gameManager.getPlayer().increaseBoardPieces();
            gameManager.getOpponent().increaseBoardPieces();
        }
        gameManager.performMove(coordinate36); // piece to move
        gameManager.performMove(coordinate06); // black forms mill
        gameManager.performMove(coordinate11); // black removes this white piece

        assertEquals(PlayerColor.WHITE, gameManager.getPlayer().getColor());
        assertEquals(Player.Phase.FLY_RULE, gameManager.getPlayer().getGamePhase());
    }

    @Test
    public void GameManager_GivenPlayerIsInFlyRulePhase_PlayerCanMovePieceAnywhere()
    {
        gameManager.getPlayer().setGamePhase(Player.Phase.FLY_RULE);

        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate66 = board.getCell(new CellPosition(6, 6));

        coordinate00.setState(CellState.BLACK);

        gameManager.performMove(coordinate00);
        gameManager.performMove(coordinate66);

        assertEquals(CellState.EMPTY, coordinate00.getCellState());
        assertEquals(CellState.BLACK, coordinate66.getCellState());
    }
}
