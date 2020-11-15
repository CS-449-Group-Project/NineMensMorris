package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class Player {
    private static final int MAX_PIECES = 9;
    private static final int NO_PIECES = 0;
    private int piecesInHand = MAX_PIECES;
    private int boardPieces;
    private final PlayerColor color;
    Phase currentPhase = Phase.PIECE_PLACEMENT;
    CellPane pieceToMove;// this is the piece a player has selected from the board during the PIECE_MOVEMENT phase that they want to move

    public Player(PlayerColor color) {

        this.color = color;
        this.reset();
    }

    public enum Phase {
        PIECE_PLACEMENT, PIECE_MOVEMENT, FLY_RULE, END_GAME
    }

    public void setGamePhase(Phase phase) {
        this.currentPhase = phase;
    }

    public CellState getPlayerColorAsCellState() {
        return color == PlayerColor.BLACK ?
                CellState.BLACK :
                CellState.WHITE;
    }

    public PlayerColor getColor() {
        return color;
    }

    public int getPiecesInHand() {
        return piecesInHand;
    }

    public boolean hasPiecesInHand() {
        return piecesInHand != NO_PIECES;
    }

    public void setPieceToMove(CellPane cell) {
        pieceToMove = cell;
    }

    public boolean hasPieceToMove() {
        return pieceToMove != null;
    }

    public void removePieceToMove() {
        pieceToMove = null;
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