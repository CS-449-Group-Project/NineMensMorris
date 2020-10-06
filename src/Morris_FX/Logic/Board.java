package Morris_FX.Logic;


import java.util.List;
import java.util.Vector;

public class Board {
    public static final int GRID_SIZE = 7;

    private Cell[][] grid;
    private InvalidCellType invalidCellType;
    private GameState gameState;

    public Board(GameState gameState) {
        this.gameState = gameState;
        createGrid();
    }

    public Cell getCell(int row, int column) {
        return this.grid[row][column];
    }

    public boolean validateMoveSelection(int row, int column) {
        Cell cell = getCell(row, column);
        Turn turn = gameState.getTurn();
        if (gameState.millFormed()) {
            switch (turn.getPlayer()) {
                case BLACK:
                    return cell.getState() == CellState.WHITE;
                case WHITE:
                    return cell.getState() == CellState.BLACK;
                default:
                    break;
            }
            return false;
        }

        if (cell.isVoid()) {
            invalidCellType = InvalidCellType.VOID;
            return false;
        }

        if (cell.isOccupied()) {
            invalidCellType = InvalidCellType.OCCUPIED;
            if (cell.isBlack() == turn.isBlack()) {
                invalidCellType = InvalidCellType.OWNED;
            }
            return false;
        }

        return true;
    }

    public void performMove(int row, int column) {
        Cell cell = getCell(row, column);
        if (gameState.millFormed()) {
            switch (cell.getState()) {
                case BLACK:
                    gameState.removePiece(Player.BLACK);
                    break;
                case WHITE:
                    gameState.removePiece(Player.WHITE);
                    break;
            }
            cell.setState(CellState.EMPTY);
        } else {
            boolean isBlackTurn = gameState.getTurn().isBlack();
            cell.setState(isBlackTurn ? CellState.BLACK : CellState.WHITE);
        }
    }

    public void reset() {
        // This assumes that invalid moves are marked as VOID already
        // which is true since this.createGrid() marks all cells as CellState.VOID.

        // mark valid spots as EMPTY
        markValidPosAsEmpty();
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

    public InvalidCellType getInvalidCellType() {
        return invalidCellType;
    }
}
