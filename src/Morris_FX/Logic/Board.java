package Morris_FX.Logic;


import Morris_FX.Ui.CellPane;

import java.util.List;
import java.util.Vector;

public class Board {
    public static final int GRID_SIZE = 7;

    private final CellPane[][] grid;
    private final GameManager gameManager;
    private boolean enableToolTip = false;
    //is this redundant? Just want to clarify initialization?

    public Board(GameManager gameManager, boolean enableToolTip) {
        this.gameManager = gameManager;
        grid = new CellPane[GRID_SIZE][GRID_SIZE];
        this.enableToolTip = enableToolTip;
        createGrid();
    }

    public Board(GameManager gameManager) {
        this.gameManager = gameManager;
        grid = new CellPane[GRID_SIZE][GRID_SIZE];
        this.enableToolTip = false;
        createGrid();
    }

    public CellPane getCell(CellPosition position) {
        int row = position.getRow(), column = position.getColumn();
        return this.grid[column][row];
    }

    public Vector<CellPane> getAllCellsWithState(CellState cellState) {
        Vector<CellPane> cellsWithState = new Vector<>(9);
        for (int i = 0; i < Board.GRID_SIZE; i++) {
            List<Integer> validMoves = getValidRowMoves(i);
            for (int j : validMoves) {
                CellPane cell = grid[i][j];
                if (cell.matches(cellState)) {
                    cellsWithState.add(cell);
                }
            }
        }
        return cellsWithState;
    }


    public boolean doesStateHaveNonMillPiece(CellState state) {
        Vector<CellPane> allPiecePlacements = getAllCellsWithState(state);
        for (CellPane cell : allPiecePlacements) {
            if (!gameManager.millFormed(cell)) {
                return true;
            }
        }
        return false;
    }

    public int getValidMoveCount(CellState state) {
        int count = 0;
        Vector<CellPane> allPiecePlacements = getAllCellsWithState(state);
        for (CellPane cell : allPiecePlacements) {
            Vector<CellPosition> validAdjacentSpots = getAdjacentSpots(cell.getPosition());
            for(CellPosition pos: validAdjacentSpots) {
                if(getCell(pos).isEmpty()) {
                    count++;
                }
            }
        }
        return count;
    }

    // Checks whether the current cell click is a valid move given the phase of the game and pieces on the board
    public boolean validateCellSelection(CellPane cell) {
        gameManager.setError("");
        Player currentPlayer = gameManager.getCurrentPlayer();
        CellState currentPlayerCellState = currentPlayer.getPlayerColorAsCellState();
        CellState opponentCellState = gameManager.getOpponentCellState();


        if (gameManager.isOver()) {
            return false;
        }
        if(gameManager.isMillFormed()){
            if (cell.matches(opponentCellState)) {
                if (!gameManager.millFormed(cell)) {
                    return true;
                } else if (!doesStateHaveNonMillPiece(opponentCellState)) {
                    return true;
                }
                gameManager.setError(cell.getPosition() + " is in a mill.");
            } else {
                gameManager.setError("Select a " + opponentCellState + " piece.");
            }
            return false;
        }

        switch (currentPlayer.currentPhase) {
            case PIECE_PLACEMENT: {
                PiecePlacementPhase piecePlacementPhase = (PiecePlacementPhase) gameManager.phaseMap.get(GameManager.phaseEnum.PIECE_PLACEMENT);
                if (piecePlacementPhase.validateCellSelection(cell, currentPlayer, currentPlayerCellState, opponentCellState)) {
                    return true;
                }
                break;
            }
            case PIECE_MOVEMENT: {
                PieceMovementPhase pieceMovementPhase = (PieceMovementPhase) gameManager.phaseMap.get(GameManager.phaseEnum.PIECE_MOVEMENT);
                if (pieceMovementPhase.validateCellSelection(cell, currentPlayer, currentPlayerCellState, opponentCellState)) {
                    return true;
                }
                break;
            }

            case FLY_RULE: { // test for fly rule case

                FlyRulePhase flyRulePhase= (FlyRulePhase) gameManager.phaseMap.get(GameManager.phaseEnum.FLY_RULE);
                if (flyRulePhase.validateCellSelection(cell, currentPlayer, currentPlayerCellState, opponentCellState)) {
                    return true;
                }
                break;

            }

            default:
              gameManager.setError(currentPlayer.currentPhase + " is not a valid phase.");
              break;
        }
        return false;
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


                if (isValidCellSpot(pos)) {
                    cellPane = new CellPane(pos, getAdjacentSpots(pos), enableToolTip);
                } else {

                    cellPane = new CellPane(pos);
                }

                grid[i][j] = cellPane;
            }
        }
        linkCells();
    }


    public void linkCells(){
        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                CellPosition pos = new CellPosition(i,j);
                CellPane cellPane = getCell(pos);

                if (cellPane.adjacentCellPositions == null) {
                    continue;
                }

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



            int offsetY = y < midpoint ? 0 : (midpoint + 1);


            int refY = y < midpoint ? y : y - (midpoint + 1);


            for(int i = Math.max(0, refY - 1); i <= Math.min(refY + 1, midpoint - 1); i++) {
                if(refY != i) {

                    validMoves.add(new CellPosition(x, i + offsetY));
                }
            }

        } else if (y == midpoint) {

            int yStepSize = Math.abs(midpoint - x);
            int upAdjacentY = y - yStepSize;
            int downAdjacentY = y + yStepSize;
            if (upAdjacentY >= 0) { // up
                validMoves.add(new CellPosition(x, upAdjacentY));
            }
            if (downAdjacentY <= max) { // down
                validMoves.add(new CellPosition(x, downAdjacentY));
            }

            int offsetX = x < midpoint ? 0 : (midpoint + 1);


            int refX = x < midpoint ? x : x - (midpoint + 1);

            for(int i = Math.max(0, refX - 1); i <= Math.min(refX + 1,midpoint - 1); i++) {
                if(refX != i) {

                    validMoves.add(new CellPosition(i + offsetX, y));
                }
            }
        } else {

            validMoves.add(new CellPosition(x, midpoint));
            validMoves.add(new CellPosition(midpoint, y));
        }
        return validMoves;
    }

}
