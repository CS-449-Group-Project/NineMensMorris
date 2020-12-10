
package Morris_FX.Logic;
import Morris_FX.Ui.CellPane;


//testing FlyRulePhase


public class FlyRulePhase implements IPhase {
    GameManager gameManager;

    FlyRulePhase(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean validateCellSelection(CellPane cell, Player currentPlayer, CellState currentPlayerCellState, CellState opponentCellState) {
        if (!currentPlayer.hasPieceToMove()) {
            if (cell.isEmpty()) {
                gameManager.setError(String.format("Select a %s piece.", currentPlayer.getColor()));
            } else if (cell.cellState.equals(currentPlayerCellState)) {
                return true;
            }
        } else if (cell.isEmpty()) {
            return true;
        }
        gameManager.setError("Select an EMPTY spot.");
        return false;

    }

    public void performMove(CellPane cellPane, Player currentPlayer) {


        if (!currentPlayer.hasPieceToMove()) {
            gameManager.setCellSelect(cellPane);
            currentPlayer.setPieceToMove(cellPane);
            return;
        }
        gameManager.setCellSelect(null);
        cellPane.setState(currentPlayer.getPlayerColorAsCellState());
        gameManager.removeMoves(cellPane);
        currentPlayer.pieceToMove.setState(CellState.EMPTY);
        gameManager.addMoves(currentPlayer.pieceToMove);
        gameManager.removePieceMoves(currentPlayer.pieceToMove);
        currentPlayer.removePieceToMove();
    }
}