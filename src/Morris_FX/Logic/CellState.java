package Morris_FX.Logic;

public enum CellState {
    EMPTY,
    VOID,
    BLACK,
    WHITE;

    public CellState complement() {
        CellState target;
        switch (this) {
            case BLACK:
                target = CellState.WHITE;
                break;
            case WHITE:
                target = CellState.BLACK;
                break;
            case EMPTY:
                target = CellState.VOID;
                break;
            default:
                target = CellState.EMPTY;
                break;
        }
        return target;
    }
}