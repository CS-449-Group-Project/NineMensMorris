package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public interface IPhase {

    boolean validateCellSelection(CellPane cell, Player currentPlayer, CellState currentPlayerCellState, CellState opponentCellState);
    void performMove(CellPane cellPane, Player currentPlayer);
    @Override
    String toString();
}
