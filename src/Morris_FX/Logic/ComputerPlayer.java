package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ComputerPlayer extends Player implements PropertyChangeListener  {
    private Board board;
    private GameManager gameManager;
    private int x, y;

    public void onComputerTurn() {

        // always performMove at least once
        CellPane cell = getCellForComputerTurn();
        System.out.println(cell.getPosition());
        gameManager.performMove(cell);

        // performMove again if not Piece Placement phase
        if (getGamePhase() != Phase.PIECE_PLACEMENT) {
            cell = getCellForComputerTurn();
            System.out.println(cell.getPosition());
            gameManager.performMove(cell);
        }

        if (gameManager.isMillFormed()) {
            cell = getCellForComputerTurn();
            System.out.println(cell.getPosition());
            gameManager.performMove(cell);
        }

        /*if (getGamePhase() == Phase.MILL_FORMED) {
            cell = getCellForComputerTurn();
            System.out.println(cell.getPosition());
            gameManager.performMove(cell);
        }*/
    }

    private CellPane getCellForComputerTurn() {
        // This position should have some logic to determine it
        CellPane cellPane;
        do {
            // generate random x and y
            x = (int) (Math.random() * board.GRID_SIZE);
            y = (int) (Math.random() * board.GRID_SIZE);
            cellPane = board.getCell(new CellPosition(x, y));
        } while (!board.validateCellSelection(cellPane));

        return cellPane;
    }

    public int[] getCoordinates() {
        int[] coordinates = new int[2];

        coordinates[0] = x;
        coordinates[1] = y;

        return coordinates;
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
