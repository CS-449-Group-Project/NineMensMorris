package Morris_FX.Logic;

public class CellPosition {
    private final int row, column;

    public CellPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public CellPosition(int index) {
        int maxWidth = Board.GRID_SIZE;
        this.column = index%maxWidth;
        this.row = (int)Math.floor(index/maxWidth);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int toIndex() {
        int maxWidth = Board.GRID_SIZE;
        return (maxWidth * row) + column;
    }
}
