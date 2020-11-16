package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

import java.util.EnumMap;
import java.util.Map;

public class GameManager {

    private Map<PlayerColor,Player> player;
    private boolean millIsFormed = false;
    private PlayerColor defaultPlayer = PlayerColor.BLACK;
    private PlayerColor currentPlayer;

    // for PIECE_PLACEMENT phase this method sets the state of the clicked cell equal to the player color; "Places current
    // player piece on the board"
    // for PIECE_MOVEMENT phase this method sets the pieceToMove variable of the current player to the clicked cell if they
    // dont have one to move already; "Selects a piece"
    // if they do have a piece to move already then it sets the state of the clicked cell to the player color, sets the
    // previously occupied cell to empty, and set pieceToMove to null in the current player object; "Places pieceToMove in new position"
    public void performMove(CellPane cellPane) {
        if (isMillFormed()) {
            cellPane.setState(CellState.EMPTY);
            getInactivePlayer().decreaseBoardPieces();
            resetMill();
            switchTurn();
            return;
        }
        Player currentPlayer = getCurrentPlayer();
        switch (currentPlayer.currentPhase) {
            case PIECE_PLACEMENT:
                currentPlayer.removePiecesFromHand();
                cellPane.setState(currentPlayer.getPlayerColorAsCellState());
                getCurrentPlayer().increaseBoardPieces();

                if (!currentPlayer.hasPiecesInHand()) {
                    currentPlayer.setGamePhase(Player.Phase.PIECE_MOVEMENT);
                }

                if(millFormed(cellPane)){
                     return;
                }
                break;
            case PIECE_MOVEMENT:
                if (!currentPlayer.hasPieceToMove()) {
                    currentPlayer.setPieceToMove(cellPane);
                    return;
                }
                cellPane.setState(currentPlayer.getPlayerColorAsCellState());
                currentPlayer.pieceToMove.setState(CellState.EMPTY);
                currentPlayer.removePieceToMove();
                if(millFormed(cellPane)){
                    return;
                }
                break;
        }
        switchTurn();
    }

    public GameManager() {
        this.currentPlayer = defaultPlayer;
        setup();
    }

    private void setup() {
        player = new EnumMap<PlayerColor, Player>(PlayerColor.class);
        for (PlayerColor playerColor : PlayerColor.values()) {

            player.put(playerColor, new Player(playerColor));
        }
    }

    public PlayerColor getCurrentPlayerColor() {
        return this.currentPlayer;
    }

    public Player getCurrentPlayer() {
        return player.get(this.currentPlayer);
    }

    public Player getInactivePlayer() {
        return player.get(this.currentPlayer.complement());
    }

    public void resetMill(){
        this.millIsFormed = false;
    }

    public void setMillIsFormedToTrue() {
        this.millIsFormed = true;
    }

    public boolean isMillFormed() {
        return this.millIsFormed;
    }

    public boolean millFormed (CellPane cell){
        CellPane recursiveCell = cell;
        int vertical = 1;
        int horizontal = 1;

        //check vertical first, check up pointer until either the reference is null or the playstate is not the 'color' of the given cell
        while(recursiveCell.up != null && recursiveCell.up.cellState == cell.cellState) {
            recursiveCell = recursiveCell.up;
            vertical++;
        }
        recursiveCell = cell;
        while(recursiveCell.down != null && recursiveCell.down.cellState == cell.cellState) {
            recursiveCell = recursiveCell.down;
            vertical++;
        }
        if (vertical == 3) {
            setMillIsFormedToTrue();
            return true;
        }
        recursiveCell = cell;

        //now check horizontal
        while(recursiveCell.left != null && recursiveCell.left.cellState == cell.cellState) {
            recursiveCell = recursiveCell.left;
            horizontal++;
        }
        recursiveCell = cell;
        while(recursiveCell.right != null && recursiveCell.right.cellState == cell.cellState) {
            recursiveCell = recursiveCell.right;
            horizontal++;
        }
        if (horizontal == 3) {
            setMillIsFormedToTrue();
            return true;
        }

        return false;
    }

    public boolean isOver() {
        return false;
    }

    public void switchTurn() {
            currentPlayer = currentPlayer.complement();
    }

    public void resetGameManager() {
        currentPlayer = defaultPlayer;
        millIsFormed = false;
    }


}
