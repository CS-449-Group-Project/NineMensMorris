package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class PieceMovementPhase implements IPhase {
    GameManager gameManager;

    PieceMovementPhase(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    public boolean validateCellSelection(CellPane cell, Player currentPlayer, CellState currentPlayerCellState, CellState opponentCellState) {

        if (!currentPlayer.hasPieceToMove()) {
            if (cell.isEmpty()) {
                gameManager.setError("Select a " + currentPlayerCellState + " marble.");
            } else if (cell.canPickup(currentPlayer)) {
                return true;
            } else if (cell.matches(currentPlayerCellState)) {
                String errorMessage = "Marble at " + cell.getPosition() + " is stuck";
                gameManager.setError(errorMessage);
            } else {
                gameManager.setError("Select a " + currentPlayerCellState + " marble.");
            }
        } else if (cell.isEmpty() && currentPlayer.pieceToMove.adjacentCells.contains(cell)) {
            // the second condition here checks the list of moves list which is populated by the linkCells method
            return true;
        }
        gameManager.setError("Select an EMPTY adjacent space.");
        return false;
    }

    public void performMove(CellPane cellPane, Player currentPlayer) {
        if (!currentPlayer.hasPieceToMove()) {
            gameManager.setCellSelect(cellPane);
            currentPlayer.setPieceToMove(cellPane);
            return;
        }
        cellPane.setState(currentPlayer.getPlayerColorAsCellState());
        gameManager.addPlacedPieceMoves(cellPane);
        gameManager.removeMoves(cellPane);
        currentPlayer.pieceToMove.setState(CellState.EMPTY);
        gameManager.addMoves(currentPlayer.pieceToMove);
        gameManager.removePieceMoves(currentPlayer.pieceToMove);
        currentPlayer.removePieceToMove();
        gameManager.setCellSelect(null);
    }
}
