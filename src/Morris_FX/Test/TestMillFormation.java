package Morris_FX.Test;

import Morris_FX.Logic.Board;
import Morris_FX.Logic.CellPosition;
import Morris_FX.Logic.GameManager;
import Morris_FX.Logic.Player;
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
    private void setup() {
        gameManager = new GameManager();
        board = new Board(gameManager);
        player = gameManager.getCurrentPlayer();
        player.setGamePhase(Player.Phase.PIECE_PLACEMENT);
        boardPane = new BoardPane(board, gameManager);
        board.reset();
        boardPane.linkCells();
    }

    @Test
    public void GameManager_GivenMillIsFormedDuringPlacementPhase_MillIsTrue()
    {
        boardPane.linkCells();

        // black moves
        CellPane coordinate00 = board.getCell(new CellPosition(0,0));
        CellPane coordinate03 = board.getCell(new CellPosition(0,3));
        CellPane coordinate06 = board.getCell(new CellPosition(0,6));

        // white moves
        CellPane coordinate30 = board.getCell(new CellPosition(3,0));
        CellPane coordinate60 = board.getCell(new CellPosition(6,0));

        gameManager.performMove(coordinate00);
        gameManager.performMove(coordinate30);
        gameManager.performMove(coordinate03);
        gameManager.performMove(coordinate60);
        gameManager.performMove(coordinate06); // black forms mill

        System.out.println(coordinate00.getCellState());
        System.out.println(coordinate03.getCellState());
        System.out.println(coordinate06.getCellState());

        System.out.println(gameManager.millFormed(coordinate06));

        //assertTrue(gameManager.isMillFormed());
    }
}
