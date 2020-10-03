package Morris_FX.Board;

import javafx.scene.layout.Pane;

public class CellUi extends Pane {
    private Cell cell;

    public CellUi(Cell cell) {
        super();
        this.cell = cell;
        //setStyle("-fx-border-color: black");
        this.setPrefSize(2000, 2000);
        this.setOnMouseClicked(e -> handleMouseClick());
    }

    private void handleMouseClick() {
        if (cell.placePiece()) {
            if (cell.isBlack()) {
                this.setStyle("-fx-background-color: blue");
            } else {
                this.setStyle("-fx-background-color: green");
            }
        }
    }
}
