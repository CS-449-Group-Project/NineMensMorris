package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class ComputerPlayer extends Player {
    private Board board;
    private GameManager gameManager;
    private int x, y;

    public void onComputerTurn() {

        // always performMove at least once
        CellPane cell = getCellForComputerTurn();
        gameManager.performMove(cell);

        // performMove again if not Piece Placement phase
        if (getGamePhase() != Phase.PIECE_PLACEMENT) {
            cell = getCellForComputerTurn();
            gameManager.performMove(cell);
        }
    }

    private CellPane getCellForComputerTurn() {
        // This position should have some logic to determine it
        CellPane cellPane;
        do {
            // generate random x and y
            x = (int) (Math.random() * board.GRID_SIZE);
            y = (int) (Math.random() * board.GRID_SIZE);
            cellPane = new CellPane(new CellPosition(x, y));
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
}
