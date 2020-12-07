package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class PiecePlacementPhase extends Phase implements IPhase {
    GameManager gameManager;

    PiecePlacementPhase(GameManager gameManager)
    {
        this.gameManager = gameManager;
    }

    public boolean validateCellSelection(CellPane cell, Player currentPlayer, CellState currentPlayerCellState, CellState opponentCellState) {
        if (cell.isOccupied()) {
            /*invalidCellType = InvalidCellType.OCCUPIED;
            if (cell.matches(currentPlayerCellState)) {
                invalidCellType = InvalidCellType.OWNED;
            }*/
            gameManager.setError("Select empty space.");
        } else {
            // invalidCellType = InvalidCellType.NONE;
            return true;
        }
        return false;
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
