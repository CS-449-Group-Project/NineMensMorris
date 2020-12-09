package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class PiecePlacementPhase implements IPhase {
    GameManager gameManager;

    PiecePlacementPhase(GameManager gameManager)
    {
        this.gameManager = gameManager;
    }

    public boolean validateCellSelection(CellPane cell, Player currentPlayer, CellState currentPlayerCellState, CellState opponentCellState) {
        if (cell.isOccupied()) {
            gameManager.setError("Select empty space.");
            return false;
        }
        return true;
    }

    public void performMove(CellPane cellPane, Player currentPlayer) {
        currentPlayer.removePiecesFromHand();
        gameManager.announceMarblesInHandChange();
        cellPane.setState(currentPlayer.getPlayerColorAsCellState());
        gameManager.getCurrentPlayer().increaseBoardPieces();
        gameManager.addPlacedPieceMoves(cellPane);
        gameManager.removeMoves(cellPane);

        if (!currentPlayer.hasPiecesInHand()) {
            if (currentPlayer.getTotalPieces() == 3) {
                currentPlayer.setGamePhase(Player.Phase.FLY_RULE);
            } else {
                currentPlayer.setGamePhase(Player.Phase.PIECE_MOVEMENT);
            }
        }
    }

}
