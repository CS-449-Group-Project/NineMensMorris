package Morris_FX.Logic;


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

    public Cell getCell(int row, int column) {
        return this.grid[row][column];
    }

    public boolean makeMove(int row, int column) {
        Cell cell = this.getCell(row, column);
        boolean valid = cell.isEmpty();
        if (valid) {
            cell.setState(turn.getTurn() ? CellState.BLACK: CellState.WHITE);
            turn.switchTurn();
        }
        return valid;
    }

    public void reset() {
        // This assumes that invalid moves are marked as VOID already
        // which is true since this.createGrid() marks all cells as CellState.VOID.

        // mark valid spots as EMPTY
        markValidPosAsEmpty();

        turn.reset();
    }

    private void createGrid() {
        grid = new Cell[GRID_SIZE][GRID_SIZE];

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
