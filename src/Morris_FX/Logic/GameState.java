package Morris_FX.Logic;

public class GameState {
    private final Turn turn;
    private int blackPieces = 0,
                whitePieces = 0;
    private boolean mill = false;

    public GameState(Turn turn) {
        this.turn = turn;
    }

    public Turn getTurn() {
        return turn;
    }

    public void removePiece(Player player) {
        switch(player) {
            case BLACK:
                this.blackPieces -= 1;
                break;
            case WHITE:
                this.whitePieces -= 1;
                break;
        }
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
        // todo reset entire game state
        turn.reset();
    }


}
