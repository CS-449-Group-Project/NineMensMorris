package Morris_FX.Logic;


import Morris_FX.Ui.CellPane;

import java.util.List;
import java.util.Vector;

public class Board {
    public static final int GRID_SIZE = 7;

    //private final Cell[][] grid;
    private final CellPane[][] grid;
    private InvalidCellType invalidCellType;
    private final GameManager gameManager;

    public Board(GameManager gameManager) {
        this.gameManager = gameManager;

        // move grid initialization here because Java
        // complains otherwise



        grid = new CellPane[GRID_SIZE][GRID_SIZE];
        createGrid();
    }

    public CellPane getCell(CellPosition position) {
        int row = position.getRow(), column = position.getColumn();
        return this.grid[row][column];
    }

    // also may want to rename to validateMoveSelection since will be having 2 cell positions
    // overload method? maybe not how will it call the other one on cell click in CellPane? conditional logic before oncellclick is defined to check
    // stage of the game
    public boolean validateCellSelection(CellPane cell) {
        //Cell cell = getCell(position);

        // always false
        if (cell.isVoid()) {
            invalidCellType = InvalidCellType.VOID;
            return false;
        }


        PlayerColor turn = gameManager.getCurrentPlayerColor();
        Player player = gameManager.getActivePlayer();
        if (gameManager.millFormed()) {

            // always false because the logic has not been added to determine if a mill was formed
            if (cell.isEmpty()) {
                invalidCellType = InvalidCellType.EMPTY;
                return false;
            }

            if (cell.is(player.getPlayerColorAsCellState().complement())) {
                invalidCellType = InvalidCellType.NONE;
                return true;
            }

            invalidCellType = InvalidCellType.OWNED;
            return false;
        }

        // switch statement here for stages of the game
        // below is my suggestion for the format of the switch statement

        switch (player.currentPhase) {
            case PIECE_PLACEMENT:
                if (cell.isOccupied()) {
                    invalidCellType = InvalidCellType.OCCUPIED;
                    if (cell.is(player.getPlayerColorAsCellState())) {
                        invalidCellType = InvalidCellType.OWNED;
                    }
                    return false;
                }

                invalidCellType = InvalidCellType.NONE;

                return true;
            case PIECE_MOVEMENT:
                if (player.hasPieceToMove()) {
                    // cell color is equal to player color
                    if (cell.cellState == player.getPlayerColorAsCellState()) {
                        // take that piece and put it in their hand
                        return true;
                        //System.out.println(player.pieceInHand.toString());
                    }
                }

            default:
                return false;
        }

//        switch (GameManager.getGameStage()) {
//            case Stage1:
                    // return false if cell is occupied
                    // break;
//            case Stage2:
                    // valid if cell is occupied
                    // store position if occupied cell is clicked in variable originalPieceLocation
                    // return false if empty cell is clicked without a value in originalPieceLocation
                    // return true if value exists in originalPieceLocation and an empty cell was clicked that is "linked"
                    // to the originally clicked cell
                    // we should have a method that returns a boolean based on whether or not the cells are linked
                    // break;
        //   case Stage3:
                    // same as Stage2 but condition for linked cells would be removed
//        }

        // this logic below would go into the first case in the switch statement above

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
