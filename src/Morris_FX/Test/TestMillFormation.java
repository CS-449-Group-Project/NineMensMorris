package Morris_FX.Test;

import Morris_FX.Logic.Board;
import Morris_FX.Logic.CellPosition;
import Morris_FX.Logic.GameManager;
import Morris_FX.Morris;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMillFormation {

    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;

    // given game in opening phase, forming a mill removes a piece

    // given game in second phase, forming a mill removes a piece

    @BeforeEach
    private void setup() {
        gameManager = new GameManager();
        board = new Board(gameManager);
        boardPane = new BoardPane(board, gameManager);
        board.reset();
    }

    @Test
    public void GameManager_TestMillFormationDuringPlacementPhase_MillIsFormed()
    {
        // black cell coordinates
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate30 = board.getCell(new CellPosition(3, 0));
        CellPane coordinate60 = board.getCell(new CellPosition(6, 0));

        // white cell coordinates
        CellPane coordinate03 = board.getCell(new CellPosition(0, 3));
        CellPane coordinate06 = board.getCell(new CellPosition(0, 6));

        gameManager.performMove(coordinate00);
        gameManager.performMove(coordinate03);
        gameManager.performMove(coordinate30);
        gameManager.performMove(coordinate06);
        gameManager.performMove(coordinate60); // black forms mill

        System.out.println(coordinate00.getCellState());
        System.out.println(coordinate30.getCellState());
        System.out.println(coordinate60.getCellState());

        System.out.println(gameManager.millFormed(coordinate60));
        //System.out.println(coordinate60.up.toString());
        //System.out.println(coordinate60.down.toString());
        //System.out.println(coordinate60.left.toString());
        //System.out.println(coordinate60.right.toString());

        System.out.println(gameManager.isMillFormed());

        // assertTrue(gameManager.isMillFormed());
    }
}
