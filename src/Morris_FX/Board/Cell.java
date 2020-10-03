package Morris_FX.Board;

public class Cell {

    private CellState playState = CellState.VOID;
    private Turn turn;


    public Cell(Turn turn) {
        this.turn = turn;
    }

    public CellState getState() {
        return this.playState;
    }

    public boolean isValid() {
        return this.playState != CellState.VOID;
    }

    public boolean isEmpty() {
        return this.playState == CellState.EMPTY;
    }

    public boolean isBlack() {
        return this.playState == CellState.BLACK;
    }

    public boolean isWhite() {
        return this.playState == CellState.WHITE;
    }

    public void setState(CellState newState) {
        playState = newState;
    }

    public boolean placePiece() {
        boolean valid = this.isValid() && this.isEmpty();
        if (valid) {
            setState(turn.getTurn() ? CellState.BLACK: CellState.WHITE);
            turn.switchTurn();
        }
        return valid;
    }

}