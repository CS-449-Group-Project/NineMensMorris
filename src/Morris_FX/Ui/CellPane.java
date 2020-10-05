package Morris_FX.Ui;

import Morris_FX.Logic.CellState;
import javafx.scene.layout.Pane;

public class CellPane extends Pane {
    private int row = -1,column = -1;
    private final BoardPane parent;

    public CellPane(BoardPane boardPane) {
        super();
        this.parent = boardPane;

        this.setPrefSize(2000, 2000);
        this.setOnMouseClicked(e -> parent.onCellClick(this, row, column));

        this.setState(CellState.VOID);
    }

    public void setPosition(int row, int column){
        this.row = row;
        this.column = column;
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
