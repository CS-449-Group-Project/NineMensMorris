package Morris_FX.Logic;


import Morris_FX.Ui.CellPane;

import java.util.List;
import java.util.Vector;

public class Board {
    public static final int GRID_SIZE = 7;

    private final CellPane[][] grid;
    private InvalidCellType invalidCellType;
    private final GameManager gameManager;

    public Board(GameManager gameManager) {
        this.gameManager = gameManager;
        grid = new CellPane[GRID_SIZE][GRID_SIZE];
        createGrid();
    }

    public CellPane getCell(CellPosition position) {
        int row = position.getRow(), column = position.getColumn();
        return this.grid[row][column];
    }

    // Checks whether the current cell click is a valid move given the phase of the game and pieces on the board
    public boolean validateCellSelection(CellPane cell) {
        if(gameManager.isMillFormed()){
            if( cell.cellState == gameManager.getInactivePlayer().getPlayerColorAsCellState() && !gameManager.millFormed(cell)) {
                return true;
            }else{
                invalidCellType = InvalidCellType.EMPTY;
                return false;
            }
        }
        if (cell.isVoid()) {
            invalidCellType = InvalidCellType.VOID;
            return false;
        }

        Player player = gameManager.getCurrentPlayer();
        if (gameManager.isMillFormed()) {


            if (cell.isEmpty()) {
                invalidCellType = InvalidCellType.EMPTY;
                return false;
            }

            if (cell.cellState.equals(player.getPlayerColorAsCellState().complement())) {
                invalidCellType = InvalidCellType.NONE;
                return true;
            }

            invalidCellType = InvalidCellType.OWNED;
            return false;
        }

        switch (player.currentPhase) {
            case PIECE_PLACEMENT:
                if (cell.isOccupied()) {
                    invalidCellType = InvalidCellType.OCCUPIED;
                    if (cell.cellState.equals(player.getPlayerColorAsCellState())) {
                        invalidCellType = InvalidCellType.OWNED;
                    }
                    return false;
                }

                invalidCellType = InvalidCellType.NONE;

                return true;
            case PIECE_MOVEMENT:
                if (!player.hasPieceToMove()) {
                    if (cell.isEmpty()) {
                        return false;
                    }
                    if (cell.canPickup(player)) {
                        if (cell.cellState == player.getPlayerColorAsCellState()) {
                            return true;
                        }
                    }
                }
                // the second condition here checks the list of moves list which is populated by the linkCells method
                if (cell.isEmpty() && player.pieceToMove.adjacentCells.contains(cell)) {
                    return true;
                }
            default:
                return false;
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
                grid[i][j] = new CellPane();
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
