package Morris_FX.Logic;

public class Player {
    private static final int MAX_PIECES = 9;
    private int pieces;
    private final PlayerColor color;

    public Player(PlayerColor color) {
        this.color = color;
    }

    CellState getCellState() {
        return color == PlayerColor.BLACK ?
                CellState.BLACK :
                CellState.WHITE;
    }

    PlayerColor getColor() {
        return color;
    }

    int getPieces() {
        return pieces;
    }

    void removePiece() {
        this.pieces -= 1;
    }

    public void reset() {
        this.pieces = Player.MAX_PIECES;
    }

}
