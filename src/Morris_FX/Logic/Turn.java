package Morris_FX.Logic;

public class Turn {
    private boolean turn = true;

    void switchTurn() {
        turn = !turn;
    }

    boolean getTurn() {
        return turn;
    }

    public void reset() {
        turn = true;
    }
}
