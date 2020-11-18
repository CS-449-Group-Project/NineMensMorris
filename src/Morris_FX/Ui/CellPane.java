package Morris_FX.Ui;

import Morris_FX.Logic.CellState;
import Morris_FX.Logic.Player;
import Morris_FX.Logic.CellPosition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.image.Image;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.tools.Tool;
import java.util.List;

public class CellPane extends Pane {
    public void setBlackPieceImage() {
        FileInputStream Piece;
        try { // "D:/UMKC_Stuff/Projects/NMM_ChooseMe/NineMensMorris/images/Morris_Board_Wood.png" -atp
            Piece = new FileInputStream("./images/BlackMarble.png");
            Image image = new Image(Piece, 40, 40, false, true);

        BackgroundImage blackPiece = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.setBackground(new Background(blackPiece));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void setWhitePieceImage() {
        FileInputStream Piece;

        try { // "D:/UMKC_Stuff/Projects/NMM_ChooseMe/NineMensMorris/images/Morris_Board_Wood.png" -atp
            Piece = new FileInputStream("./images/WhiteMarble.png");
            Image image = new Image(Piece, 40, 40, false, true);

            BackgroundImage whitePiece = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);

            this.setBackground(new Background(whitePiece));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
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
        this.setOnMouseClicked(e -> {
            // alert error invalid cell
            parent.onVoidCellClicked();
        });
        setState(initialState);
    }

    public CellPane(CellPosition position, List<CellPosition> adjacentCellPositions, boolean enableToolTip) {
        this.position = position;
        this.adjacentCellPositions = adjacentCellPositions;
        this.parent = null;
        this.setPrefSize(2000, 2000);
        Tooltip toolTip;
        if (enableToolTip) {
            toolTip = new Tooltip(position.toString());
            this.setOnMouseMoved(mouseEvent -> {
                double x = mouseEvent.getScreenX() + 15;
                double y = mouseEvent.getScreenY() + 0;
                toolTip.show((Node)mouseEvent.getSource(),x,y);
            });

            this.setOnMouseExited(e -> {
                toolTip.hide();
            });
        }
        this.setOnMouseClicked(e -> {
            boolean secondary = false;
            if (e.getButton() == MouseButton.SECONDARY) {
                secondary = true;
            }
            parent.onCellClick(this);
        });

        this.initialState = CellState.EMPTY;
        setState(initialState);
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

                setBlackPieceImage();
                this.cellState = state;
                break;
            case WHITE:
                setWhitePieceImage();
                this.cellState = state;
                break;
            case VOID:
            case EMPTY:
                setBackground(null);
                this.cellState = state;
                break;
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

    public BoardPane getParentPane() {
        return parent;
    }
}

