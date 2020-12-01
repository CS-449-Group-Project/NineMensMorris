package Morris_FX.Test;


import Morris_FX.Logic.CellPosition;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestCellPosition {
    @Test
    public void rightDirectionAdjacentCell() {
        CellPosition pos00 = new CellPosition(0,0);
        CellPosition pos30 = new CellPosition(3,0);
        assertEquals(pos00.directionOf(pos30), "RIGHT");
    }

    @Test
    public void leftDirectionAdjacentCell() {
        CellPosition pos00 = new CellPosition(0,0);
        CellPosition pos30 = new CellPosition(3,0);
        assertEquals(pos30.directionOf(pos00), "LEFT");
    }

    @Test
    public void upDirectionAdjacentCell() {
        CellPosition pos00 = new CellPosition(0,0);
        CellPosition pos03 = new CellPosition(0,3);
        assertEquals(pos03.directionOf(pos00), "UP");
    }

    @Test
    public void downDirectionAdjacentCell() {
        CellPosition pos00 = new CellPosition(0,0);
        CellPosition pos03 = new CellPosition(0,3);
        assertEquals(pos00.directionOf(pos03), "DOWN");
    }
}
