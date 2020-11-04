package Morris_FX.Logic;


import java.util.List;
import java.util.Vector;

public class Board {
    public static final int GRID_SIZE = 7;

    private final Cell[][] grid;
    private InvalidCellType invalidCellType;
    private final GameState gameState;

    public Board(GameState gameState) {
        this.gameState = gameState;

        // move grid initialization here because Java
        // complains otherwise

        grid = new Cell[GRID_SIZE][GRID_SIZE];
        createGrid();
    }

    public Cell getCell(CellPosition position) {
        int row = position.getRow(), column = position.getColumn();
        return this.grid[row][column];
    }

    public boolean validateMoveSelection(CellPosition position) {
        Cell cell = getCell(position);

        // always false
        if (cell.isVoid()) {
            invalidCellType = InvalidCellType.VOID;
            return false;
        }


        Turn turn = gameState.getTurn();
        Player player = gameState.getActivePlayer();
        if (gameState.millFormed()) {

            // always false
            if (cell.isEmpty()) {
                invalidCellType = InvalidCellType.EMPTY;
                return false;
            }

            if (cell.is(player.getCellState().complement())) {
                invalidCellType = InvalidCellType.NONE;
                return true;
            }

            invalidCellType = InvalidCellType.OWNED;
            return false;
        }

        if (cell.isOccupied()) {
            invalidCellType = InvalidCellType.OCCUPIED;
            if (cell.is(player.getCellState())) {
                invalidCellType = InvalidCellType.OWNED;
            }
            return false;
        }

        invalidCellType = InvalidCellType.NONE;

        return true;
    }


    public void performMove(CellPosition position) {
        Cell cell = getCell(position);
        Player player = gameState.getActivePlayer();
        if (gameState.millFormed()) {
            gameState.getInactivePlayer().removePiece();
            cell.setState(CellState.EMPTY);
        } else {
            cell.setState(player.getCellState());
        }
    }

    public void reset() {
        // This assumes that invalid moves are marked as CellState.VOID already
        // which is true since this.createGrid() marks all cells as CellState.VOID.

        // mark valid spots as CellState.EMPTY
        markValidPosAsEmpty();
    }

    private void createGrid() {
        // let i be vertical, j be horizontal
        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    private void markValidPosAsEmpty() {
        for (int i = 0; i < Board.GRID_SIZE; i++) {
            List<Integer> validMoves = getValidRowMoves(i);
            for (int j : validMoves) {
                grid[i][j].setState(CellState.EMPTY);
            }
        }
    }

    /*public boolean isValidMove(int row, int column) {
        return !getCell(row, column).isVoid();
    }*/

    public List<Integer> getValidRowMoves(int row) { // row loops from 0 to 6
        List<Integer> rowMoves = new Vector<>(Board.GRID_SIZE - 1);
        int middle = (Board.GRID_SIZE - 1)/2;
        if (row == middle) {
            for (int i = 0; i < Board.GRID_SIZE; i++) {
                if (i != middle) {
                    rowMoves.add(i);
                }
            }
        } else {
            int dist = Math.abs(row - middle);
            for(int i = 0; i < 3; i++) {
                int offset = (i - 1) * dist;
                rowMoves.add(offset + middle);
                // at row = 0, dist = 3
                // at i = 0, offset = -3
                // add (-3 + 3) => row, column (0,0)

                // at i = 1, offset = 0
                // add (0 + 3) => row, column (0,3)

                // at i = 2, offset = 3
                // add (3 + 3) => row, column (0,6)

                // at row = 1, dist = 2
            }
        }
        return rowMoves;
    }

    public InvalidCellType getInvalidCellType() {
        return invalidCellType;
    }
}
