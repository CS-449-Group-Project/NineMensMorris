package Morris_FX.Logic;


import Morris_FX.Ui.CellPane;
import javafx.util.Pair;

import java.util.*;

public class Board {
    public static final int GRID_SIZE = 7;

    private final CellPane[][] grid;

    private final GameManager gameManager;
    private boolean enableToolTip = false;


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
            if (!MillChecker.millFormed(cell)) {
                return true;
            }
        }
        return false;
    }

    public Vector<CellPane> findAllNonMillPiece(CellState state) {
        Vector<CellPane> allPiecePlacements = getAllCellsWithState(state);
        Vector<CellPane> nonMills = new Vector<>();
        for (CellPane cell : allPiecePlacements) {
            if (!MillChecker.millFormed(cell)) {
                nonMills.add(cell);
            }
        }
        return nonMills;
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
        Player currentPlayer = gameManager.getPlayer();
        CellState currentPlayerCellState = currentPlayer.getPlayerColorAsCellState();
        CellState opponentCellState = gameManager.getOpponentCellState();


        if (gameManager.isOver()) {
            return false;
        }
        if(gameManager.getPlayer().isMillFormed()){
            if (cell.matches(opponentCellState)) {
                if (!MillChecker.millFormed(cell)) {
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
                gameManager.setError("Select an EMPTY adjacent space.");
                break;
            }

            case FLY_RULE: { // test for fly rule case

                FlyRulePhase flyRulePhase= (FlyRulePhase) gameManager.phaseMap.get(GameManager.phaseEnum.FLY_RULE);
                if (flyRulePhase.validateCellSelection(cell, currentPlayer, currentPlayerCellState, opponentCellState)) {
                    return true;
                }
                gameManager.setError("Select an EMPTY spot.");
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

    // I copied this from my NMM implementation with some modifications
    public Vector<CellPosition> getAdjacentSpots(CellPosition from) {
        int x = from.getColumn(),y=from.getRow();

        int midpoint = GRID_SIZE/2;
        int max = midpoint * 2;
        Vector<CellPosition> validMoves = new Vector<>(4);

        // midpoint values have 3 or 4 valid moves
        if (x == midpoint) {
            // this difference between each step in vertical is midpoint - y
            int xStepSize = Math.abs(midpoint - y);
            int leftAdjacentX = x - xStepSize;
            int rightAdjacentX = x + xStepSize;
            if (leftAdjacentX >= 0) {
                validMoves.add(new CellPosition(leftAdjacentX, y));
            }
            if (rightAdjacentX <= max) {
                validMoves.add(new CellPosition(rightAdjacentX, y));
            }


            // starts at either : y = 0 or y = 4
            int offsetY = y < midpoint ? 0 : (midpoint + 1);


            // this normalizes it so they speak the same language
            int refY = y < midpoint ? y : y - (midpoint + 1);


            for(int i = Math.max(0, refY - 1); i <= Math.min(refY + 1, midpoint - 1); i++) {
                if(refY != i) {
                    // offset is reapplied to make it valid again
                    validMoves.add(new CellPosition(x, i + offsetY));
                }
            }

        } else if (y == midpoint) {
            // this difference between each step in horizontal is midpoint - x
            int yStepSize = Math.abs(midpoint - x);
            int upAdjacentY = y - yStepSize;
            int downAdjacentY = y + yStepSize;
            if (upAdjacentY >= 0) { // up
                validMoves.add(new CellPosition(x, upAdjacentY));
            }
            if (downAdjacentY <= max) { // down
                validMoves.add(new CellPosition(x, downAdjacentY));
            }
            // starts at either : x = 0 or x = 4
            int offsetX = x < midpoint ? 0 : (midpoint + 1);

            // this normalizes it so they speak the same language
            int refX = x < midpoint ? x : x - (midpoint + 1);

            for(int i = Math.max(0, refX - 1); i <= Math.min(refX + 1,midpoint - 1); i++) {
                if(refX != i) {
                    // offset is reapplied to make it valid again
                    validMoves.add(new CellPosition(i + offsetX, y));
                }
            }
        } else {
            // a corner only has two valid positions which are located
            // at the midpoints
            validMoves.add(new CellPosition(x, midpoint));
            validMoves.add(new CellPosition(midpoint, y));
        }
        return validMoves;
    }

    public Vector<CellPane> getVacantMillCells(CellPane pane) {
        // first check if missing CellPositions are all empty
        Vector<CellPane> allVacant = new Vector<>();
        CellPosition panePos = pane.getPosition();
        Vector<CellPosition> horizontalPos = findMissingHorizontalMillCoordsFor(panePos);
        Vector<CellPane> vacantHorizontal = new Vector<>();
        for (CellPosition pos: horizontalPos) {
            CellPane paneOfFocus = getCell(pos);
            if (paneOfFocus.isEmpty()) {
                vacantHorizontal.add(paneOfFocus);
            }
        }
        if (vacantHorizontal.size() == horizontalPos.size()) {
            allVacant.addAll(vacantHorizontal);
        }

        CellPosition flippedPos = panePos.flip();
        Vector<CellPosition> verticalPos = findMissingHorizontalMillCoordsFor(flippedPos);
        Vector<CellPane> vacantVertical = new Vector<>();

        for (CellPosition pos: verticalPos) {
            CellPane paneOfFocus = getCell(pos.flip());
            if (paneOfFocus.isEmpty()) {
                vacantVertical.add(paneOfFocus);
            }
        }

        if (vacantVertical.size() == verticalPos.size()) {
            allVacant.addAll(vacantVertical);
        }
        return allVacant;
    }

    public Vector<CellPosition> findMissingHorizontalMillCoordsFor(CellPosition pos) {
        int x = pos.getColumn(), y = pos.getRow();
        Vector<CellPosition> millCellPositions = new Vector<>();
        if (y == 3) {
            List<Integer> validRowMoves = getValidRowMoves(y);
            boolean belowMiddle = x < 3;
            for (Integer row : validRowMoves) {
                boolean rowBelowMiddle = (row < 3);
                if (row == x) {
                    continue;
                }
                if (rowBelowMiddle == belowMiddle) {
                    millCellPositions.add(new CellPosition(x, y));
                }
            }
        } else {
            List<Integer> validRowMoves = getValidRowMoves(y);
            for (Integer row : validRowMoves) {
                if (row == x) {
                    continue;
                }
                millCellPositions.add(new CellPosition(x, y));
            }
        }

        return millCellPositions;
    }

    public Vector<CellPane> getPotentialMillCells(CellState color) {
        Vector<CellPane> potentialMillSpots = new Vector<>();
        for (CellPane cell: getAllValidEmptyCells()) {
            if (MillChecker.millFormed(cell, color))   {
                potentialMillSpots.add(cell);
            }
        }
        return potentialMillSpots;
    }


    public Pair<CellPane, Integer> firstPieceOnShortestPath(CellPane source, CellPane target) {
        return firstPieceOnShortestPath(source, target, new HashMap<>());
    }

    private Pair<CellPane, Integer> firstPieceOnShortestPath(CellPane source, CellPane target, HashMap<CellPane, Pair<CellPane, Integer>> visited) {
        if (source == target) {
            return new Pair<>(null, 0);
        }

        int lowestDistance = Integer.MAX_VALUE;
        CellPane bestChoice = null;
        for (CellPane pane : source.adjacentCells) {
            Pair<CellPane, Integer> childPair;

            // what we want
             if (pane == target) {
                bestChoice = pane;
                lowestDistance = 1;
                break;
            } else if (visited.containsKey(pane)) {
                 // been visited, get cached value and make decision
                childPair = visited.get(pane);
            } else if (pane.isEmpty()) {
                // empty can explore
                visited.put(pane, null);
                childPair = firstPieceOnShortestPath(pane, target, visited);
                visited.put(pane, childPair); // update with true value
            } else { //  not empty, ignore those
                continue;
            }

            // check prevents parent cells or bad cells
            // from being considered
            if (childPair != null) {
                int distance = childPair.getValue();
                if (distance < lowestDistance) {
                    lowestDistance = distance;
                    bestChoice = pane;
                }
            }
        }

        // could not find a good path
        if (lowestDistance == Integer.MAX_VALUE) {
            return null;
        }

        return new Pair<>(bestChoice, lowestDistance + 1);
    }

    public Vector<CellPosition> getEmptyAdjacentSpots(CellPosition from) {
        Vector<CellPosition> emptySpots = new Vector<>();
        for(CellPosition pos: getAdjacentSpots(from)) {
            if (getCell(pos).isEmpty()) {
                emptySpots.add(pos);
            }
        }
        return emptySpots;
    }

    public ArrayList<CellPane> getAllValidCells() {
        ArrayList<CellPane> validCells = new ArrayList<>(GRID_SIZE);
        for (int i = 0; i < GRID_SIZE; i++) {
            for(int j = 0; j < GRID_SIZE; j++) {
                CellPosition pos = new CellPosition(i,j);
                if (isValidCellSpot(pos)) {
                    validCells.add(getCell(pos));
                }
            }
        }
        return validCells;
    }

    public ArrayList<CellPane> getAllValidEmptyCells() {
        ArrayList<CellPane> validEmptyCells = new ArrayList<>(GRID_SIZE);
        for (CellPane cell : getAllValidCells()) {
            if (cell.isEmpty()) {
                validEmptyCells.add(cell);
            }
        }
        return validEmptyCells;
    }

}