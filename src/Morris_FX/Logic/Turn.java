package Morris_FX.Logic;

public class Turn {
    private Player defaultPlayer;
    private Player turn;

    public Turn(Player defaultPlayer) {
        this.defaultPlayer = defaultPlayer;
        turn = defaultPlayer;
    }

    public Player getPlayer() {
        return turn;
    }

    public void setDefaultPlayer(Player defaultPlayer) {
        this.defaultPlayer = defaultPlayer;
    }

    void switchTurn() {
        turn = turn == Player.BLACK ? Player.WHITE : Player.BLACK;
    }

    boolean isBlack() { return turn == Player.BLACK; }

    boolean isWhite() { return turn == Player.WHITE; }

    public void reset() {
        turn = defaultPlayer;
    }
}
