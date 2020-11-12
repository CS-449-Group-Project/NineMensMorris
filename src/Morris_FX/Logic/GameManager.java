package Morris_FX.Logic;

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
    Phase currentPhase = Phase.PIECE_PLACEMENT;

    public enum Phase {
        PIECE_PLACEMENT, PIECE_MOVEMENT, FLY_RULE, END_GAME
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

    public void setGamePhase(Phase phase) {
        this.currentPhase = phase;
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
