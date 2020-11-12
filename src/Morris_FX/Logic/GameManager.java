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
    Phase currentPhase = Phase.PIECE_PLACEMENT;

    // we will definitely use board later in this methdd
    public void performMove(CellPane cellPane, Board board) {
        Player player = getActivePlayer();
        if (player.hasPiecesInHand()) {
            // sets the game state every time performMove is called
            // this is so that if a player reaches the Fly rule before their opponent, the opponent doesnt also enter
            // that game phase
            // One way we may be able to update this is to move the game phase enumerated list to the Player class so that
            // each player has an independent game phase
            setGamePhase(Phase.PIECE_PLACEMENT);
            player.removePiecesFromHand();
            if (millFormed()) {
                // gameState.getInactivePlayer().removeDeckPieces();
                cellPane.setState(CellState.EMPTY);
            } else {
                cellPane.setState(player.getCellState());
            }
        } else {
            // Piece Movement
            setGamePhase(Phase.PIECE_MOVEMENT);
            if (player.getBoardPieces() == 3) {
                // fly rule
                // run special method
            }
        }
        switchTurn();
    }

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
