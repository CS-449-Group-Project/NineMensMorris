package Morris_FX.Ui;

import Morris_FX.Logic.CellState;
import Morris_FX.Logic.Player;
import Morris_FX.Logic.CellPosition;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class CellPane extends Pane {
    private final BoardPane parent;
    private CellPosition position;

    // could call above instead of up
    public CellPane up;
    // could call below instead of down
    public CellPane down;
    public CellPane left;
    public CellPane right;

    public CellState cellState;

    public List<CellPane> adjacentCells = new ArrayList<>();

    public CellPane(CellPosition position, BoardPane boardPane) {
        super();
        this.parent = boardPane;
        this.position = position;
        this.setPrefSize(2000, 2000);
        this.setOnMouseClicked(e -> parent.onCellClick(this));
        this.setState(CellState.VOID);
    }

    // this is only exists to satisfy what is going on in the board class, I personally don't see a reason for it
    public CellPane() {
        this.cellState = CellState.VOID;
        this.position = null;
        this.parent = null;
    }

    public CellPosition getPosition() {
        return position;
    }

    public CellState getCellState() {
        return this.cellState;
    }

    public void setState(CellState state) {
        switch (state) {
            case BLACK:
                setStyle("-fx-background-color: black; -fx-background-radius: 100");
                this.cellState = state;
                break;
            case WHITE:
                setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 4; -fx-background-radius: 100; -fx-border-radius: 100;");
                this.cellState = state;
                break;
            case VOID:
                setStyle("-fx-background-color: transparent");
                this.cellState = state;
                break;
            case EMPTY:
                setStyle(null);
                this.cellState = state;
                break;
        }
    }

    public boolean isVoid() { return this.cellState == CellState.VOID; }

    public boolean isEmpty() {
        return this.cellState == CellState.EMPTY;
    }

    public boolean isBlack() {
        return this.cellState == CellState.BLACK;
    }

    public boolean isWhite() { return this.cellState == CellState.WHITE; }

    public boolean isOccupied() { return this.isBlack() || this.isWhite(); }

    //this checks if a piece can be moved. Checks the cell you're picking the marble up from
    //if the cell above is empty, the move counter increases. checks up, down, left, right
    //if no valid moves, returns false and says no possible moves
    public boolean canChoose(){
        int counter = 0;
        if(this.up != null && this.up.cellState == CellState.EMPTY) {
            counter++;
        }
        if(this.down != null && this.down.cellState == CellState.EMPTY) {
            counter++;
        }
        if(this.left != null && this.left.cellState == CellState.EMPTY) {
            counter++;
        }
        if(this.right != null && this.right.cellState == CellState.EMPTY) {
            counter++;
        }
        if(counter > 0) {
            System.out.println("you may move");
            return true;
        }
        else {
            System.out.println("no possible moves");
            return false;
        }
    }

    //check if you can pick up a marble from a cell
    //first check if you're picking up a marble from a cell that has the same playState as the player (ex BLACK == BLACK)
    //AND call moveCheck to see if there are available moves to make
    public boolean canPickup(Player currentPlayer){
        if(this.cellState == currentPlayer.getPlayerColorAsCellState() && canChoose()){
            if(!currentPlayer.hasPieceToMove()){
                return true;
            }
        }
        return false;
    }

    public boolean matches(CellState cellState) {
        return this.cellState.equals(cellState);
    }

}

