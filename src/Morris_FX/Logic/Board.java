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
        if (player.hasMarblesInHand()) {
            player.removeMarblesFromHand();
            if (gameState.millFormed()) {
                // gameState.getInactivePlayer().removeDeckMarbles();
                cell.setState(CellState.EMPTY);
            } else {
                cell.setState(player.getCellState());
            }
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



    public List<CellPosition> getValidMoves(CellPosition pos) {
        Vector<CellPosition> validMoves = new Vector<CellPosition>(4);
        int midPoint = (Board.GRID_SIZE - 1)/2;
        CellPosition reference;
        double angle = 0;
        if (isCenterPosition(pos)) {
            // reference point
            int nonMidPoint = pos.getRow() == midPoint ?
                                                pos.getColumn() :
                                                pos.getRow();

            int diff = midPoint - Math.abs(midPoint - nonMidPoint);

            reference = new CellPosition(diff, midPoint);

            int xDiff[] = {-1,1};
            for (int i = 0; i < xDiff.length;i++) {
                int multiplier = -1 + 2 * i;
                int x = reference.getColumn(),
                    y = midPoint + multiplier * diff;
                validMoves.add(new CellPosition(x, y));
            }

            int start = Math.max(0, reference.getColumn() - 1);
            int end = Math.min(2, reference.getColumn() + 1);
            for(int i = start; i <= end; i++) {
                if (i != reference.getColumn()) {
                    validMoves.add(new CellPosition(i, reference.getRow()));
                }
            }
        } else { // corner position
            int diff = midPoint - Math.abs(midPoint - pos.getRow());

            reference = new CellPosition(diff, diff);
            for (int i = 0; i < 2;i++) {
                int x = midPoint * Math.abs(i - 1) + diff * i;
                int y = diff * Math.abs(i - 1) + midPoint * i;
                validMoves.add(new CellPosition(x, y));
            }
        }


        angle = reference.angleTo(pos);
        
        for(int i = validMoves.size() - 1; i >=0 ; i--) {
            CellPosition adjustedPos = validMoves.get(i).rotateCounterClockwise(Math.toRadians(angle));
            if (getCell(adjustedPos).isOccupied()) {
                validMoves.remove(i);
            } else {
                validMoves.set(i, adjustedPos);
            }

        }
        return validMoves;
    }

    private boolean isCenterPosition(CellPosition pos) {
        int middle = (Board.GRID_SIZE - 1)/2;
        return pos.getRow() == middle || pos.getColumn() == middle;
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
