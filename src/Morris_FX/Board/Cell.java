package Morris_FX.Board;

import javafx.scene.layout.Pane;

import Morris_FX.Board.Turn;
import Morris_FX.Board.CellState;

//class to hold cells
public class Cell extends Pane {

    public CellState playState = CellState.VOID;
    private Turn turn;

    public Cell(Turn turn) {
        super();
        this.turn = turn;
        //setStyle("-fx-border-color: black");
        this.setPrefSize(2000, 2000);
        this.setOnMouseClicked(e -> handleMouseClick());
    }


    private void handleMouseClick() {
        if (this.playState == CellState.EMPTY)
            if (turn.getTurn()) {
                this.setStyle("-fx-background-color: blue");
            }else{
                this.setStyle("-fx-background-color: green");
            }
        turn.switchTurn();
    }

}