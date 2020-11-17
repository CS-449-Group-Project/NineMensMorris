package Morris_FX.Ui;

import Morris_FX.Logic.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class BoardPane extends GridPane {
    private final Board board;
    private final GameManager gameManager;
    public BoardPane(Board board, GameManager gameManager) {
        super();
        this.board = board;
        this.gameManager = gameManager;
        setup();
    }

    public void setup() {
        this.setupBackgroundImage();
        this.setupGrid();
    }

    private void setupBackgroundImage() {
        FileInputStream backgroundImage;
        try{ // "D:/UMKC_Stuff/Projects/NMM_ChooseMe/NineMensMorris/images/Morris_Board_Wood.png" -atp
            backgroundImage = new FileInputStream("./images/Morris_Board_Wood.png");
            Image image = new Image(backgroundImage,550,550,false,true);

            BackgroundImage emptyBoard = new BackgroundImage(image,
                    BackgroundRepeat.REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);

            this.setBackground(new Background(emptyBoard));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void setupGrid() {
        for (int row = 0; row < Board.GRID_SIZE; row++)
            for (int column = 0; column < Board.GRID_SIZE; column++) {
                CellPane cellPane = board.getCell(new CellPosition(column, row));
                cellPane.setParentPane(this);
                add(cellPane, column, row);
            }
    }

    public void onCellClick(CellPane cell) {
        if (board.validateCellSelection(cell)) {
            gameManager.performMove(cell);
        } else {
            System.out.println("Invalid cell type: " + board.getInvalidCellType());
        }
    }

}
