package Morris_FX.Logic;

public class TurnContext {
    private Player player;
    private Player opponent;
    public TurnContext(Player defaultPlayer, Player defaultOpponent) {
        player = defaultPlayer;
        opponent = defaultOpponent;
    }

    public void switchPlayers() {
        Player currentPlayer = opponent;
        opponent = player;
        player = currentPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }
}
