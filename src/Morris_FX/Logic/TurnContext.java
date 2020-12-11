package Morris_FX.Logic;

public class TurnContext extends ObservableObject {
    private Player defaultPlayer;
    private Player defaultOpponent;
    private Player player;
    private Player opponent;
    public TurnContext(Player defaultPlayer, Player defaultOpponent) {
        this.defaultPlayer = defaultPlayer;
        this.defaultOpponent = defaultOpponent;
        setPlayerOpponent(defaultPlayer, defaultOpponent);
    }

    private void setPlayerOpponent(Player player, Player opponent) {
        this.player = player;
        this.opponent = opponent;
        firePropertyChange("player", player);
    }

    public void reannounceCurrentPlayer() {
        firePropertyChange("player", player);
    }

    public void switchPlayers() {
        setPlayerOpponent(opponent, player);
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void reset() {
        setPlayerOpponent(defaultPlayer, defaultOpponent);
    }
}
