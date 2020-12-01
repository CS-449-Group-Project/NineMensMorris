package Morris_FX.Test;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.CellPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

//jUnit version 5.4+

public class TestGameBoardSetup {

    private Board board;
    private GameManager gameManager;
    private BoardPane boardPane;
    // format of test method names: public void Class_GivenScenario_Expectation()

    @BeforeEach
    private void setup() {
        gameManager = new GameManager();
        board = new Board(gameManager);
        boardPane = new BoardPane(board, gameManager);
        board.reset();
    }

    @Test
    public void Board_GivenNewGame_EmptyCells() {
        CellPane coordinate00 = board.getCell(new CellPosition(0, 0));
        CellPane coordinate11 = board.getCell(new CellPosition(1, 1));
        CellPane coordinate22 = board.getCell(new CellPosition(2, 2));

        assertEquals(CellState.EMPTY, coordinate00.getCellState());
        assertEquals(CellState.EMPTY, coordinate11.getCellState());
        assertEquals(CellState.EMPTY, coordinate22.getCellState());
    }

    @Test
    public void Board_GivesValidAdjacentCellPositions() {
        CellPosition cell03 = new CellPosition(0,3);
        Vector<CellPosition> cellSpots = board.getAdjacentSpots(cell03);
        assertTrue(inVector(cellSpots, new CellPosition(0,0)));
        assertTrue(inVector(cellSpots, new CellPosition(0,6)));
        assertTrue(inVector(cellSpots, new CellPosition(1,3)));
        assertFalse(inVector(cellSpots, new CellPosition(3,3)));
        assertEquals(3,cellSpots.size());
    }

    @Test
    public void Board_GivesValidAdjacentCellPositionsTowardsCenter() {
        CellPosition cell03 = new CellPosition(2,3);
        Vector<CellPosition> cellSpots = board.getAdjacentSpots(cell03);
        assertTrue(inVector(cellSpots, new CellPosition(1,3)));
        assertTrue(inVector(cellSpots, new CellPosition(2,2)));
        assertTrue(inVector(cellSpots, new CellPosition(2,4)));
        assertFalse(inVector(cellSpots, new CellPosition(3,3)));
        assertEquals(3,cellSpots.size());

        CellPosition cell32 = new CellPosition(3,2);
        Vector<CellPosition> cellSpots2 = board.getAdjacentSpots(cell32);
        assertTrue(inVector(cellSpots2, new CellPosition(3,1)));
        assertTrue(inVector(cellSpots2, new CellPosition(2,2)));
        assertTrue(inVector(cellSpots2, new CellPosition(4,2)));
        assertFalse(inVector(cellSpots2, new CellPosition(3,3)));
        assertEquals(3,cellSpots2.size());
    }

    @Test
    public void Board_GivesValidAdjacentCellPositionsMiddleCenter() {
        CellPosition cell03 = new CellPosition(3,5);
        Vector<CellPosition> cellSpots = board.getAdjacentSpots(cell03);
        assertTrue(inVector(cellSpots, new CellPosition(3,6)));
        assertTrue(inVector(cellSpots, new CellPosition(3,4)));
        assertTrue(inVector(cellSpots, new CellPosition(5,5)));
        assertTrue(inVector(cellSpots, new CellPosition(1,5)));
        assertEquals(4,cellSpots.size());


        CellPosition cell53 = new CellPosition(5,3);
        Vector<CellPosition> cellSpots2 = board.getAdjacentSpots(cell53);
        assertTrue(inVector(cellSpots2, new CellPosition(6,3)));
        assertTrue(inVector(cellSpots2, new CellPosition(4,3)));
        assertTrue(inVector(cellSpots2, new CellPosition(5,5)));
        assertTrue(inVector(cellSpots2, new CellPosition(5,1)));
        assertEquals(4,cellSpots2.size());
    }

    private boolean inVector(Vector<CellPosition> vector, CellPosition other) {
        return vector.stream().filter(adjacentPosition -> {
            return adjacentPosition.matches(other);
        }).count() == 1;
    }

    @Test
    public void Board_GivenNewGame_VoidCells() {
        CellPane coordinate33 = board.getCell(new CellPosition(3, 3));
        CellPane coordinate12 = board.getCell(new CellPosition(1, 2));

        assertEquals(CellState.VOID, coordinate33.getCellState());
        assertEquals(CellState.VOID, coordinate12.getCellState());
    }

    @Test
    public void Board_GivenNewGame_TurnIsBlack() {
        assertEquals(PlayerColor.BLACK, gameManager.getCurrentPlayerColor());
    }
}
