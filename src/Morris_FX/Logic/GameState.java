package Morris_FX.Logic;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.util.EnumMap;
import java.util.Map;

public class GameState {
    private final Turn turn;
    private Map<PlayerColor,Player> player;
    private boolean mill = false;

    public GameState(Turn turn) {
        this.turn = turn;
        setup();
    }

    private void setup() {
        player = new EnumMap<PlayerColor, Player>(PlayerColor.class);
        for (PlayerColor playerColor : PlayerColor.values()) {

            player.put(playerColor, new Player(playerColor));
        }
    }

    public Turn getTurn() {
        return turn;
    }

    public Player getActivePlayer() {
        return player.get(turn.getPlayerColor());
    }

    public Player getInactivePlayer() {
        return player.get(turn.getPlayerColor().complement());
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
            turn.switchTurn();
        }
    }

    public void reset() {
        // TODO: reset entire game state
        turn.reset();
    }

}
