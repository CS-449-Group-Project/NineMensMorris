package Morris_FX.Ui;

import Morris_FX.Logic.CellPosition;
import Morris_FX.Logic.CellState;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class CellPane extends Pane {
    private final CellPosition position;
    private final BoardPane parent;

    // could call above instead of up
    public CellPane up;
    // could call below instead of down
    public CellPane down;
    public CellPane left;
    public CellPane right;

    public CellState cellState;

    public java.util.List<CellPane> moves = new ArrayList<CellPane>();

    public CellPane(CellPosition position, BoardPane boardPane) {
        super();
        this.parent = boardPane;
        this.position = position;
        this.setPrefSize(2000, 2000);
        // put conditional logic here that gets the stage of the game and place the following line within the stage 1 condition
        this.setOnMouseClicked(e -> parent.onCellClick(this));
        this.setState(CellState.VOID);
    }

    public CellPosition getPosition() {
        return position;
    }

    // This method now sets the style and the cellState variable for CellPane
    // This now means that cellState exists in two different classes, CellPane and Cell
    // We need to remove the cellState property from the Cell class
    // Removing the cellState from the Cell class will involve looking through the code to see where it is used and updating
    // it to use this classes equivalent property `cellState` instead
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
}

