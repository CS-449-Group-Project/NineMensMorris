package Morris_FX.Ui;

import Morris_FX.Logic.CellPosition;
import Morris_FX.Logic.CellState;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class CellPane extends Pane {
    private final CellPosition position;
    private final BoardPane parent;

    public CellPane up;
    public CellPane down;
    public CellPane left;
    public CellPane right;

    public java.util.List<CellPane> moves = new ArrayList<CellPane>();

    public CellPane(CellPosition position, BoardPane boardPane) {
        super();
        this.parent = boardPane;
        this.position = position;
        this.setPrefSize(2000, 2000);
        this.setOnMouseClicked(e -> parent.onCellClick(this));
        this.setState(CellState.VOID);
    }

    public CellPosition getPosition() {
        return position;
    }

    public void setState(CellState state) {
        switch(state) {
            case BLACK:
                setStyle("-fx-background-color: black; -fx-background-radius: 100");
                break;
            case WHITE:
                setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 4; -fx-background-radius: 100; -fx-border-radius: 100;");
                break;
            case VOID:
                setStyle("-fx-background-color: transparent");
                break;
            case EMPTY:
                setStyle(null);
                break;
        }
    }

}
