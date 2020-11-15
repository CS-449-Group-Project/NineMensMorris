package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

import java.util.EnumMap;
import java.util.Map;

public class GameManager {

    private Map<PlayerColor,Player> player;
    private boolean mill = false;
    private PlayerColor defaultPlayer = PlayerColor.BLACK;
    private PlayerColor currentPlayer;

    // for PIECE_PLACEMENT phase this method sets the state of the clicked cell equal to the player color; "Places current
    // player piece on the board"
    // for PIECE_MOVEMENT phase this method sets the pieceToMove variable of the current player to the clicked cell if they
    // dont have one to move already; "Selects a piece"
    // if they do have a piece to move already then it sets the state of the clicked cell to the player color, sets the
    // previously occupied cell to empty, and set pieceToMove to null in the current player object; "Places pieceToMove in new position"
    public void performMove(CellPane cellPane) {
        Player currentPlayer = getActivePlayer();
        switch (currentPlayer.currentPhase) {
            case PIECE_PLACEMENT:
                currentPlayer.removePiecesFromHand();
                cellPane.setState(currentPlayer.getPlayerColorAsCellState());
//                if (millFormed()) {
//                    // gameState.getInactivePlayer().removeBoardPieces();
//                    cellPane.setState(CellState.EMPTY);
//                }
                if (!currentPlayer.hasPiecesInHand()) {
                    currentPlayer.setGamePhase(Player.Phase.PIECE_MOVEMENT);
                }
                break;
            case PIECE_MOVEMENT:
                if (!currentPlayer.hasPieceToMove()) {
                    currentPlayer.setPieceToMove(cellPane);
                    return;
                }
                cellPane.setState(currentPlayer.getPlayerColorAsCellState());
                currentPlayer.pieceToMove.setState(CellState.EMPTY);
                currentPlayer.removePieceToMove();
                break;
        }
        switchTurn();
    }

    public GameManager() {
        this.currentPlayer = defaultPlayer;
        setup();
    }

    private void setup() {
        player = new EnumMap<PlayerColor, Player>(PlayerColor.class);
        for (PlayerColor playerColor : PlayerColor.values()) {

            player.put(playerColor, new Player(playerColor));
        }
    }

    public PlayerColor getCurrentPlayerColor() {
        return this.currentPlayer;
    }

    public Player getActivePlayer() {
        return player.get(this.currentPlayer);
    }

    Player getInactivePlayer() {
        return player.get(this.currentPlayer.complement());
    }

    public void setMill(boolean formedMill) {
        this.mill = formedMill;
    }

    public boolean millFormed() {
        return this.mill;
    }

    public boolean isOver() {
        return false;
    }

    public void switchTurn() {
        if (!millFormed()) {
            currentPlayer = currentPlayer.complement();
        }
    }

    public void reset() {
        currentPlayer = defaultPlayer;
    }


}
