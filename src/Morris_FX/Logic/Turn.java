package Morris_FX.Logic;

public class Turn {
    private PlayerColor defaultPlayer;
    private PlayerColor turn;

    public Turn(PlayerColor defaultPlayer) {
        this.defaultPlayer = defaultPlayer;
        turn = defaultPlayer;
    }

    public PlayerColor getPlayerColor() {
        return turn;
    }

    public void setDefaultPlayer(PlayerColor defaultPlayer) {
        this.defaultPlayer = defaultPlayer;
    }

    void switchTurn() {
        turn = turn.complement();
    }

    boolean isBlack() { return turn == PlayerColor.BLACK; }

    boolean isWhite() { return turn == PlayerColor.WHITE; }

    public void reset() {
        turn = defaultPlayer;
    }
}
