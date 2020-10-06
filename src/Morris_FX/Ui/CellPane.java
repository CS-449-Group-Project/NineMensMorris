package Morris_FX.Ui;

import Morris_FX.Logic.CellPosition;
import Morris_FX.Logic.CellState;
import javafx.scene.layout.Pane;

public class CellPane extends Pane {
    private final CellPosition position;
    private final BoardPane parent;

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
                setStyle("-fx-background-color: black");
                break;
            case WHITE:
                setStyle("-fx-background-color: white");
                break;
            case VOID:
                setStyle("-fx-background-color: grey");
                break;
            case EMPTY:
                setStyle(null);
                break;
        }
    }

}
