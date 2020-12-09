package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMillFormation {

    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;
    private Player player;

    @BeforeEach
    private void setup()
    {
        gameManager = new GameManager();
        board = new Board(gameManager);


        boardPane = new BoardPane(board, gameManager);
        board.reset();
        player = gameManager.getPlayer();
    }

    private CellPane createCellPane(CellPosition pos, CellState state) {
        CellPane pane = new CellPane(pos);
        if (state != null) pane.setState(state);
        return pane;
    }
    @Test
    public void GameManager_GivenAllPlayersHaveOnlyMillsFormed_AnOpponentsPieceCanBeRemoved() {
        CellPosition[] positions = {
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
        }

        assertTrue(gameManager.isMillFormed());

        assertEquals(PlayerColor.WHITE, gameManager.getCurrentPlayerColor());
        assertTrue(board.validateCellSelection(board.getCell(positions[0])));
    }

    // Class_GivenScenario_Expectation()

    @Test
    public void GameManager_GivenMillIsFormedDuringPlacementPhase_PieceIsRemoved()
    {
        // black cell coordinates
        gameManager.getPlayer().setGamePhase(Player.Phase.PIECE_PLACEMENT);

        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate30 = board.getCell(new CellPosition(3, 0));
        CellPane coordinate60 = board.getCell(new CellPosition(6, 0));

        // white cell coordinates
        gameManager.getOpponent().setGamePhase(Player.Phase.PIECE_PLACEMENT);
        CellPane coordinate03 = board.getCell(new CellPosition(0, 3));
        CellPane coordinate06 = board.getCell(new CellPosition(0, 6));

        gameManager.performMove(coordinate00);

        gameManager.performMove(coordinate03);
        gameManager.performMove(coordinate30);
        gameManager.performMove(coordinate06);
        gameManager.performMove(coordinate60); // black forms mill

        assertTrue(gameManager.isMillFormed());
    }


    @Test
    public void GameManager_GivenMillIsFormedDuringMovementPhase_PieceIsRemoved()
    {
        player.setGamePhase(Player.Phase.PIECE_MOVEMENT);

        // black cell coordinates
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate30 = board.getCell(new CellPosition(3, 0));
        CellPane coordinate63 = board.getCell(new CellPosition(6, 3));

        // white cell coordinates
        CellPane coordinate03 = board.getCell(new CellPosition(0, 3));
        CellPane coordinate06 = board.getCell(new CellPosition(0, 6));
        CellPane coordinate34 = board.getCell(new CellPosition(3, 4));

        // cell which will form mill for black
        CellPane coordinate60 = board.getCell(new CellPosition(6, 0));

        // set up game scenario
        coordinate00.setState(CellState.BLACK);
        coordinate30.setState(CellState.BLACK);
        coordinate63.setState(CellState.BLACK);
        coordinate03.setState(CellState.WHITE);
        coordinate06.setState(CellState.WHITE);
        coordinate34.setState(CellState.WHITE);

        // black moves piece to form mill
        gameManager.performMove(coordinate63);
        gameManager.performMove(coordinate60);

        assertTrue(gameManager.isMillFormed());
    }

}
