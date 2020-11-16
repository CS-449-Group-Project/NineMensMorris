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
        Player currentPlayer = gameManager.getCurrentPlayer();
        CellState currentPlayerCellState = currentPlayer.getPlayerColorAsCellState();
        CellState opponentCellState = gameManager.getOpponentCellState();

        if(gameManager.isMillFormed()){
            if( cell.matches(opponentCellState) && !gameManager.millFormed(cell)) {
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

        switch (currentPlayer.currentPhase) {
            case PIECE_PLACEMENT:
                if (cell.isOccupied()) {
                    invalidCellType = InvalidCellType.OCCUPIED;
                    if (cell.matches(currentPlayerCellState)) {
                        invalidCellType = InvalidCellType.OWNED;
                    }
                    return false;
                }

                invalidCellType = InvalidCellType.NONE;

                return true;
            case PIECE_MOVEMENT:
                if (!currentPlayer.hasPieceToMove()) {
                    if (cell.isEmpty()) {
                        return false;
                    }
                    if (cell.canPickup(currentPlayer)) {
                        if (cell.matches(currentPlayerCellState)) {
                            return true;
                        }
                    }
                }
                // the second condition here checks the list of moves list which is populated by the linkCells method
                if (cell.isEmpty() && currentPlayer.pieceToMove.adjacentCells.contains(cell)) {
                    return true;
                }
            default:
                return false;
        }
    }


    public void reset() {
        // This assumes that invalid moves are marked as CellState.VOID already
        // which is true since this.createGrid() marks all cells as CellState.VOID.

        for (int i = 0; i < Board.GRID_SIZE; i++) {
            List<Integer> validMoves = getValidRowMoves(i);
            for (int j : validMoves) {
                grid[i][j].reset();
            }
        }
    }


    private void createGrid() {
        // let i be vertical, j be horizontal
        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                CellPosition pos = new CellPosition(i,j);
                CellPane cellPane;

                // valid spots should always store adjacent cell positions spots
                if (isValidCellSpot(pos)) {
                    cellPane = new CellPane(pos, getAdjacentSpots(pos));
                } else {
                    // invalid should not
                    cellPane = new CellPane(pos);
                }
                grid[i][j] = cellPane;
            }
        }
        linkCells();
    }

    //takes all the find functions and iterates over the entire board
    //for each playable cell make a pointer to the cell up, right, down, and left of that cell
    //also add that linked cell to the list of playable cells for each cell
    //this list is used to check to find a place to move
    public void linkCells(){
        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                CellPosition pos = new CellPosition(i,j);
                CellPane cellPane = getCell(pos);
                // Is a void cell
                if (cellPane.adjacentCellPositions == null) {
                    continue;
                }

                // this assumes that all cells will update their own
                // record for adjacent cells so no need to worry about
                // linking the current cell to the other one
                Vector<CellPane> adjacentCells = new Vector<>();
                for (CellPosition adjacentPos : cellPane.adjacentCellPositions) {
                    CellPane adjacentCellPane = getCell(adjacentPos);
                    String targetPositionDirection = pos.directionOf(adjacentPos);
                    cellPane.setDirectionalCellPane(targetPositionDirection, adjacentCellPane);
                    adjacentCells.add(adjacentCellPane);
                }
                cellPane.adjacentCells = adjacentCells;
            }
        }
    }

    private boolean isValidCellSpot(CellPosition pos) {
        int x = pos.getRow(),y = pos.getColumn();
        int midpoint = GRID_SIZE/2;
        int max = GRID_SIZE - 1;

        if (x == midpoint) {
            return y != midpoint;
        } else if (y == midpoint) {
            return true;
        }
        // x and y can not be midpoints so the checks can be skipped
        // negativeSlopeDiagonal || positiveSlopeDiagonal
        return x == y || (x + y == max);
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

    // I copied this from my NMM implementation with some modifications
    public Vector<CellPosition> getAdjacentSpots(CellPosition from) {
        int x = from.getColumn(),y=from.getRow();

        int midpoint = GRID_SIZE/2;
        int max = midpoint * 2;
        Vector<CellPosition> validMoves = new Vector<>(4);

        if (x == midpoint) {
            int xStepSize = Math.abs(midpoint - y);
            int leftAdjacentX = x - xStepSize;
            int rightAdjacentX = x + xStepSize;
            if (leftAdjacentX >= 0) {
                validMoves.add(new CellPosition(leftAdjacentX, y));
            }
            if (rightAdjacentX <= max) {
                validMoves.add(new CellPosition(rightAdjacentX, y));
            }

            int offsetY = y < midpoint ? 0 : midpoint;
            int refY = y < midpoint ? y : Math.abs(midpoint - y);

            for(int i = Math.max(0, refY - 1); i <= Math.min(refY + 1, midpoint); i++) {
                if(refY != i) {
                    validMoves.add(new CellPosition(x, i + offsetY));
                }
            }

        } else {
            validMoves.add(new CellPosition(midpoint, y));
        }

        if (y == midpoint) {
            int yStepSize = Math.abs(midpoint - x);
            int upAdjacentY = y - yStepSize;
            int downAdjacentY = y + yStepSize;
            if (upAdjacentY >= 0) {
                validMoves.add(new CellPosition(x, upAdjacentY));
            }
            if (downAdjacentY <= max) {
                validMoves.add(new CellPosition(x, downAdjacentY));
            }

            int offsetX = x < midpoint ? 0 : midpoint;
            int refX = x < midpoint ? x : Math.abs(midpoint - x);
            for(int i = Math.max(0, refX - 1); i <= Math.min(refX + 1,midpoint); i++) {
                if(refX != i) {
                    validMoves.add(new CellPosition(i + offsetX, y));
                }
            }
        } else {
            validMoves.add(new CellPosition(x, midpoint));
        }
        return validMoves;
    }

}
