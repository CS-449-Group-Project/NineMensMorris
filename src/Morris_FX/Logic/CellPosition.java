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

            return x2 < x1 ? "LEFT" : "RIGHT";
        }


        return y2 < y1 ? "UP" : "DOWN";
    }

    @Override
    public String toString() {
        return String.format("%s (%d,%d)",getHumanString(), column, row);
    }

    public String getHumanString() {
        char character = (char)(column + 0x41);
        return String.format("%c%d", character, (Board.GRID_SIZE - row));
    }
}
