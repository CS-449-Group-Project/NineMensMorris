package Morris_FX.Logic;

public class CellPosition {
    private final int row, column;

    public CellPosition(int column, int row) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean matches(CellPosition other) {
        return row == other.getRow() && column == other.getColumn();
    }

    public String directionOf(CellPosition adjacentPos) {

        int x1 = column, x2= adjacentPos.getColumn();
        int y1 = row, y2 = adjacentPos.getRow();

        if (y1 == y2) {
            // targetPosition <- currentPosition (x2 < x1)
            // currentPosition -> targetPosition (x1 < x2)
            return x2 < x1 ? "LEFT" : "RIGHT";
        }

        // targetPosition <- currentPosition (y2 < y1) UP
        // currentPosition -> targetPosition (y1 < y2) DOWN

        return y2 < y1 ? "UP" : "DOWN";
    }

    @Override
    public String toString() {
        char character = (char)(column + 0x41);
        return String.format("%c%d (%d,%d)", character, (Board.GRID_SIZE - row), column, row);
    }
}
