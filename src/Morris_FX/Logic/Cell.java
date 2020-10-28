package Morris_FX.Logic;

public class Cell {

    private CellState playState = CellState.VOID;

    public CellState getState() { return this.playState; }
    public boolean is(CellState state) { return this.playState == state; }

    public boolean isVoid() { return this.playState == CellState.VOID; }

    public boolean isEmpty() {
        return this.playState == CellState.EMPTY;
    }

    public boolean isBlack() {
        return this.playState == CellState.BLACK;
    }

    public boolean isWhite() { return this.playState == CellState.WHITE; }

    public boolean isOccupied() { return this.isBlack() || this.isWhite(); }

    public void setState(CellState newState) {
        playState = newState;
    }

    public void reset() {
        this.playState = CellState.VOID;
    }
}