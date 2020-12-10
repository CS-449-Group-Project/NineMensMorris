package Morris_FX.Ui;

import Morris_FX.Logic.*;
import Morris_FX.Morris;
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

    public void setupBackgroundImage() {
        FileInputStream wood;
        FileInputStream jade;
        FileInputStream marble;
        try{ // "D:/UMKC_Stuff/Projects/NMM_ChooseMe/NineMensMorris/images/Morris_Board_Wood.png" -atp
            if(Morris.currentBoard == Morris.BoardOption.WOOD) {
                wood = new FileInputStream("./images/Morris_Board_Wood.png");

                Image image = new Image(wood, 550, 550, false, true);

                BackgroundImage emptyBoard = new BackgroundImage(image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT);

                this.setBackground(new Background(emptyBoard));
            }
            if(Morris.currentBoard == Morris.BoardOption.JADE) {
                jade = new FileInputStream("./images/Morris_Board_Jade.png");

                Image image = new Image(jade, 550, 550, false, true);

                BackgroundImage emptyBoard = new BackgroundImage(image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT);

                this.setBackground(new Background(emptyBoard));
            }
            if(Morris.currentBoard == Morris.BoardOption.MARBLE) {
                marble = new FileInputStream("./images/Morris_Board_Marble.png");

                Image image = new Image(marble, 550, 550, false, true);

                BackgroundImage emptyBoard = new BackgroundImage(image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT);

                this.setBackground(new Background(emptyBoard));
            }
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
                cellPane.setMaxSize(74,74);
                cellPane.setMinSize(74,74);
                cellPane.setParentPane(this);
                add(cellPane, column, row);
            }
    }

    public void onCellClick(CellPane cell) {
        if (gameManager.isComputerPlayerTurn()) {
            return;
        }


        if (board.validateCellSelection(cell)) {
            gameManager.performMove(cell);
        }
    }

    public void onVoidCellClicked() {
        gameManager.setError("Invalid cell selection.");
    }

    public Board getBoard() {
        return board;
    }
}
