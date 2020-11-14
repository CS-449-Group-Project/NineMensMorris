package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

import java.util.EnumMap;
import java.util.Map;

public class GameManager {

    // game stages enumerated list for piece placement stage, etc..
    // getGameStage method
    //private final Turn turn;
    private Map<PlayerColor,Player> player;
    private boolean mill = false;
    private PlayerColor defaultPlayer = PlayerColor.BLACK;
    private PlayerColor currentPlayer;

    // we will definitely use board later in this method
    public void performMove(CellPane cellPane, Board board) {
        Player player = getActivePlayer();
        // place the putting piece in hand in the piece movement section of performMove
        switch (player.currentPhase) {
            case PIECE_PLACEMENT:
                player.removePiecesFromHand();
                cellPane.setState(player.getPlayerColorAsCellState());
                if (millFormed()) {
                    // gameState.getInactivePlayer().removeDeckPieces();
                    cellPane.setState(CellState.EMPTY);
                }
                if (!player.hasPiecesInHand()) {
                    player.setGamePhase(Player.Phase.PIECE_MOVEMENT);
                }
                break;
            case PIECE_MOVEMENT:
                if (!player.hasPieceToMove()) {
                    player.setPieceToMove(cellPane);
                    return;
                }
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
