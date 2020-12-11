package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

public class ComputerPlayer extends Player implements PropertyChangeListener  {
    private final Board board;
    private final GameManager gameManager;

    public void onComputerTurn() {
        Phase phase = getGamePhase();
        Vector<CellPane> bestMoves = null;
        if (!millFormed) {
            switch(phase) {
                case PIECE_PLACEMENT:
                    bestMoves = ComputerBrain.getOptimalPiecePlacement(board, this);
                    break;
                case PIECE_MOVEMENT: {
                    if (pieceToMove == null) {
                        bestMoves = ComputerBrain.getOptimalPieceSelection(board, this);
                    } else {
                        bestMoves = ComputerBrain.getOptimalPieceMovement(board, this);
                    }
                    break;
                }
                case FLY_RULE: {
                    if (pieceToMove == null) {
                        bestMoves = ComputerBrain.getOptimalPieceFlySelection(board, this);
                    } else {
                        bestMoves = ComputerBrain.getOptimalPieceFlyMovement(board, this);
                    }
                    break;
                }
                default:
                    bestMoves = null;
                    break;

            }
        } else {
            bestMoves = ComputerBrain.getOptimalPieceToRemove(board, this);
        }

        if (bestMoves == null || bestMoves.size() == 0) {
            CellPane cell = getCellForComputerTurn();
            System.out.println(phase + " => (Random)" + cell.getPosition());
            gameManager.performMove(cell);
        } else {
            int choice = (int) (Math.random() * bestMoves.size());
            CellPane bestChoice = bestMoves.get(choice);
            System.out.println(phase + " => " + bestChoice.getPosition());
            gameManager.performMove(bestChoice);
        }
    }

    private CellPane getCellForComputerTurn() {
        // This position should have some logic to determine it
        CellPane cellPane;
        int x,y;
        do {
            // generate random x and y
            x = (int) (Math.random() * Board.GRID_SIZE);
            y = (int) (Math.random() * Board.GRID_SIZE);
            cellPane = board.getCell(new CellPosition(x, y));
        } while (!board.validateCellSelection(cellPane));

        return cellPane;
    }

    public ComputerPlayer(PlayerColor color, Board board, GameManager gameManager) {
        super(color);
        this.board = board;
        this.gameManager = gameManager;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("player".equals(propertyName)) {
            if (evt.getNewValue() == this) {
                onComputerTurn();
            }
        }
    }
}
