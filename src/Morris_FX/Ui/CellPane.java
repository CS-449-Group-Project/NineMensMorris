package Morris_FX.Ui;

import Morris_FX.Logic.CellPosition;
import Morris_FX.Logic.CellState;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class CellPane extends Pane {
    private final CellPosition position;
    private final BoardPane parent;

    //private Image blackPieceImage = new Image("https://www.flaticon.com/free-icon/black-circle_14", 50, 50, false, true);
    //private BackgroundImage blackPiece = new BackgroundImage(blackPieceImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

    //private Image whitePieceImage = new Image("https://www.iconspng.com/image/75653/white-circle", 50, 50, false, true);
    //private BackgroundImage whitePiece = new BackgroundImage(whitePieceImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);


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
