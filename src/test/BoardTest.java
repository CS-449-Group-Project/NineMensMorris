package test;

import Morris_FX.Logic.*;
import Morris_FX.Morris;
import junit.framework.TestCase;

public class BoardTest extends TestCase {

    private Board testBoard;

    protected void setUp() throws Exception {
        super.setUp();
        Turn turn = new Turn(PlayerColor.BLACK);
        GameState gameState = new GameState(turn);
        testBoard = new Board(gameState);
    }

    public void testGetCellValid(){
        CellPosition cellposition = new CellPosition(1, 1);
        Cell[][] grid;
        grid = new Cell[1][1];
        assertEquals(testBoard.getCell(cellposition), grid);
    }

//    public void testGetCellInvalid(){
//        CellPosition cellposition = new CellPosition(-1, -1);
//
//    }
}
