package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class Player {
    private static final int MAX_MARBLES = 9;
    private static final int NO_MARBLES = 0;
    private int marblesInHand = MAX_MARBLES;
    private int boardPieces;
    private final PlayerColor color;
    CellPane inPlay;

    public Player(PlayerColor color) {

        this.color = color;
        this.reset();
    }

    CellState getCellState() {
        return color == PlayerColor.BLACK ?
                CellState.BLACK :
                CellState.WHITE;
    }

    PlayerColor getColor() {
        return color;
    }

    public int getMarblesInHand() {
        return marblesInHand;
    }


    public boolean hasMarblesInHand() {
        return marblesInHand != NO_MARBLES;
    }

    public void removeMarblesFromHand() {
        marblesInHand--;
    }

    public int getBoardPieces(){

        return boardPieces;
    }

    public void reset() {
        marblesInHand = MAX_MARBLES;
        boardPieces = NO_MARBLES;
    }

}
