package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class Player {
    private static final int MAX_PIECES = 9;
    private static final int NO_PIECES = 0;
    private int piecesInHand = MAX_PIECES;
    private int boardPieces;
    private final PlayerColor color;
    // this is the piece a player has picked up from the board during the PIECE_MOVEMENT phase
    CellPane pieceInHand;

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

    public int getPiecesInHand() {
        return piecesInHand;
    }


    public boolean hasPiecesInHand() {
        return piecesInHand != NO_PIECES;
    }

    public void removePiecesFromHand() {
        piecesInHand--;
    }

    public int getBoardPieces(){

        return boardPieces;
    }

    public void reset() {
        piecesInHand = MAX_PIECES;
        boardPieces = NO_PIECES;
    }

}