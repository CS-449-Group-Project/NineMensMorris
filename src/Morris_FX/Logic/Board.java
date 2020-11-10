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

    public boolean validateCellSelection(CellPosition position) {
        Cell cell = getCell(position);

        // always false
        if (cell.isVoid()) {
            invalidCellType = InvalidCellType.VOID;
            return false;
        }


        Turn turn = gameState.getTurn();
        Player player = gameState.getActivePlayer();
        if (gameState.millFormed()) {

            // always false because the logic has not been added to determine if a mill was formed
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

        // switch statement here for stages of the game
        // below is my suggestion for the format of the switch statement

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
        }else if(player.getBoardPieces() > 3){
            // Marble Movement

        }else{
            //Fly Rule
        }
        gameState.switchTurn();


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
