<<<<<<< Updated upstream
package Morris_FX.Logic;

public class Player {
    private static final int MAX_MARBLES = 9;
    private static final int NO_MARBLES = 0;
    private int marblesInHand = MAX_MARBLES;
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

    public int getMarblesInHand() {
        return marblesInHand;
    }


    public boolean hasMarblesInHand() {
        return marblesInHand != NO_MARBLES;
    }

    public void removeMarblesFromHand() {
        marblesInHand--;
    }

    public void reset() {
        marblesInHand = MAX_MARBLES;
        boardPieces = NO_MARBLES;
    }

=======
package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class Player {
    private static final int MAX_PIECES = 9;
    private static final int NO_PIECES = 0;
    private int piecesInHand = MAX_PIECES;
    private int boardPieces;
    private final PlayerColor color;
    Phase currentPhase = Phase.PIECE_PLACEMENT;

    public int validMovesCounter = 0;
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

    public void increaseBoardPieces(){
        boardPieces++;
    }

    public void decreaseBoardPieces(){
        boardPieces--;
    }

    public int getBoardPieces(){

        return boardPieces;
    }

    public void reset() {
        piecesInHand = MAX_PIECES;
        boardPieces = NO_PIECES;
    }

>>>>>>> Stashed changes
}