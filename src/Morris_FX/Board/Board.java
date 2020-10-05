package Morris_FX.Board;


import java.util.List;
import java.util.Vector;

public class Board {
    public static final int GRID_SIZE = 7;
    private final Turn turn;
    private Cell[][] grid;

    public Board() {
        this.turn = new Turn();
        this.createGrid();
    }

    public Cell getCell(int x, int y) {
        return this.grid[y][x];
    }

    public void reset() {
        // This assumes that invalid moves are marked as VOID already
        // which is true since this.createGrid() marks all cells as CellState.VOID.

        // mark valid spots as EMPTY
        this.markValidPosAsEmpty();
    }

    private void createGrid() {
        grid = new Cell[GRID_SIZE][GRID_SIZE];

        // let i be vertical, j be horizontal
        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new Cell(turn);
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

    public boolean isValidMove(int row, int column) {
        return !getCell(row, column).isVoid();
    }

    public List<Integer> getValidRowMoves(int row) {
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
            }
        }
        return rowMoves;
    }
}
