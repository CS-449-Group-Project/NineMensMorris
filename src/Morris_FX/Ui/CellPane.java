package Morris_FX.Ui;

import Morris_FX.Logic.CellState;
import Morris_FX.Logic.Player;
import Morris_FX.Logic.CellPosition;
import javafx.scene.control.Cell;
import javafx.scene.layout.*;

import java.util.List;

public class CellPane extends Pane {
    private BoardPane parent;
    // initialState won't change if the cell position does not change
    private CellState initialState = CellState.VOID;
    private CellPosition position;

    // could call above instead of up
    public CellPane up;
    // could call below instead of down
    public CellPane down;
    public CellPane left;
    public CellPane right;

    public CellState cellState;

    public List<CellPosition> adjacentCellPositions = null;
    public List<CellPane> adjacentCells = null;

    public CellPane(CellPosition position) {
        this.cellState = CellState.VOID;
        this.position = position;
        this.setState(initialState);
    }

    public CellPane(CellPosition position, List<CellPosition> adjacentCellPositions) {
        this.position = position;
        this.adjacentCellPositions = adjacentCellPositions;
        this.parent = null;
        this.setPrefSize(2000, 2000);
        this.setOnMouseClicked(e -> parent.onCellClick(this));
        this.initialState = CellState.EMPTY;
        this.setState(initialState);
    }

    public void setParentPane(BoardPane boardPane) {
        this.parent = boardPane;
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
        updateAdjacentCells();
    }

    private void updateAdjacentCells() {
        // assume there was none set to begin with
        if (cellState == CellState.VOID) {
            return;
        }

        boolean isEmpty = cellState == CellState.EMPTY;

        for(CellPane adjacentCell : adjacentCells) {
            CellPosition adjacentCellPosition = adjacentCell.getPosition();
            String direction = position.directionOf(adjacentCellPosition);
            String oppositeDirection = adjacentCellPosition.directionOf(position);
            if (isEmpty) {
                adjacentCell.setDirectionalCellPane(oppositeDirection, null);
                setDirectionalCellPane(direction, null);
            } else {
                adjacentCell.setDirectionalCellPane(oppositeDirection, this);
                setDirectionalCellPane(direction, adjacentCell);
            }
        }
    }

    public void setDirectionalCellPane(String targetPositionDirection, CellPane adjacentCellPane) {
        switch (targetPositionDirection) {
            case "LEFT":
                left = adjacentCellPane;
                break;
            case "RIGHT":
                right = adjacentCellPane;
                break;
            case "UP":
                up = adjacentCellPane;
                break;
            case "DOWN":
                down = adjacentCellPane;
                break;
            default:
                break;
        }
    }

    public CellPane getDirectionalCellPane(String direction) {
        CellPane targetCellPane;
        switch (direction) {
            case "LEFT":
                targetCellPane = left;
                break;
            case "RIGHT":
                targetCellPane = right;
                break;
            case "UP":
                targetCellPane = up ;
                break;
            case "DOWN":
                targetCellPane = down;
                break;
            default:
                targetCellPane = null;
                break;
        }
        return targetCellPane;
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


    public void reset() {
        // directional fields are automatically update when this is called
        setState(initialState);
    }
}

