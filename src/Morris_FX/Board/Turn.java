package Morris_FX.Board;

public class Turn {
    private boolean turn = false;

    void switchTurn() {
        turn = !turn;
    }

    boolean getTurn() {
        return turn;
    }
}
