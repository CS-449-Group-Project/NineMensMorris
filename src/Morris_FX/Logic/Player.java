package Morris_FX.Logic;

public class Player {
    private static final int MAX_MARBLES = 9;
    private static final int NO_MARBLES = 0;
    private int deckMarbles = MAX_MARBLES;
    private int boardPieces;
    private final PlayerColor color;

    public Player(PlayerColor color) {

        this.color = color;
        this.reset();
    }

    CellState getCellState() {
        return color == PlayerColor.BLACK ?
                CellState.BLACK :
                CellState.WHITE;
    }

    PlayerColor getColor() {
        return color;
    }

    public int getDeckMarbles() {
        return deckMarbles;
    }


    public boolean removeDeckMarbles() {
        boolean canRemovePiece = deckMarbles > NO_MARBLES;
        if (canRemovePiece) {
            deckMarbles--;
        }
        return canRemovePiece;
    }

    public void reset() {
        deckMarbles = MAX_MARBLES;
        boardPieces = NO_MARBLES;
    }

}
