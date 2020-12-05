package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class Player extends ObservableObject {
  private static final int MAX_PIECES = 9;
  private static final int NO_PIECES = 0;

  protected int piecesInHand;
  private int boardPieces;
  private int removedPieces = 0;
  private final PlayerColor color;
  public Phase currentPhase = Phase.PIECE_PLACEMENT;

  public int validMovesCounter = 0;
  public CellPane pieceToMove; // this is the piece a player has selected from the board during the
                   // PIECE_MOVEMENT phase that they want to move

  public Player(PlayerColor color) {
    this.color = color;
    setPiecesInHand(MAX_PIECES);
  }

  public int getTotalPieces() {
    return piecesInHand + boardPieces;
  }

  public enum Phase {
    PIECE_PLACEMENT,
    PIECE_MOVEMENT,
    FLY_RULE,
    MILL_FORMED,
    GAME_OVER;
    @Override
    public String toString() {
      String phaseName;
      switch(this) {
        case PIECE_PLACEMENT:
          phaseName = "Marble Placement";
          break;
        case PIECE_MOVEMENT:
          phaseName = "Marble Move";
          break;
        case FLY_RULE:
          phaseName = "Marble Fly";
          break;
        case MILL_FORMED:
          phaseName = "Mill formed";
          break;
        default:
          phaseName = "End Game";
          break;
      }
      return phaseName;
    }
  }

  public void setGamePhase(Phase phase) {
    this.currentPhase = phase;
  }

  public Phase getGamePhase() {
    return this.currentPhase;
  }

  public CellState getPlayerColorAsCellState() {
    return color == PlayerColor.BLACK ? CellState.BLACK : CellState.WHITE;
  }

  public PlayerColor getColor() {
    return color;
  }

  public int getPiecesInHand() {
    return piecesInHand;
  }

  public void setPiecesInHand(int pieces) {
    String propertyName = "piecesInHand";
    firePropertyChange(propertyName, pieces);
    piecesInHand = pieces;
  }

  public boolean hasPiecesInHand() {
    return piecesInHand > NO_PIECES;
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
    setPiecesInHand(piecesInHand - 1);
  }

  public void increaseBoardPieces() {
    boardPieces++;
  }

  public void decreaseBoardPieces() {
    boardPieces--;
    removedPieces++;
  }

  public int getBoardPieces() {
    return boardPieces;
  }

  public void reset() {
    setPiecesInHand(MAX_PIECES);
    boardPieces = NO_PIECES;
    this.setGamePhase(Phase.PIECE_PLACEMENT);
    this.removePieceToMove();
  }
}