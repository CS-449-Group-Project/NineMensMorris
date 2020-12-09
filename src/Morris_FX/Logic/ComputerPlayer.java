package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class ComputerPlayer extends Player {
    private Board board;
    private GameManager gameManager;

    public void onComputerTurn() {
        CellPane cell = getCellForComputerTurn();

    }

    private CellPane getCellForComputerTurn() {
        // This position should have some logic to determine it
        CellPosition pos = new CellPosition(0,0);
        //

        return new CellPane(pos);
    }

    public ComputerPlayer(PlayerColor color, Board board, GameManager gameManager) {
        super(color);
        this.board = board;
        this.gameManager = gameManager;
    }
}
