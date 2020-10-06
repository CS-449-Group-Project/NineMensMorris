package Morris_FX.Ui;

import Morris_FX.Logic.Board;
import Morris_FX.Logic.CellPosition;
import Morris_FX.Logic.CellState;
import Morris_FX.Logic.GameState;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.List;

public class BoardPane extends GridPane {
    private final Board board;
    private final GameState gameState;
    private CellPane[][] grid;
    public BoardPane(Board board, GameState gameState) {
        super();
        this.board = board;
        this.gameState = gameState;
        setup();
    }

    public void setup() {
        this.setupBackgroundImage();
        this.setupGrid();
    }

    private void setupBackgroundImage() {
        //create the background image from a url. Need changing the basis to the file
        Image image = new Image("https://www.iconfinder.com/data/icons/toys-2/512/game-6-512.png",550,450,false,true);

        BackgroundImage emptyBoard = new BackgroundImage(image,
                                                        BackgroundRepeat.REPEAT,
                                                        BackgroundRepeat.NO_REPEAT,
                                                        BackgroundPosition.DEFAULT,
                                                        BackgroundSize.DEFAULT);

        this.setBackground(new Background(emptyBoard));
    }

    private void setupGrid() {
        grid = new CellPane[Board.GRID_SIZE][Board.GRID_SIZE];
        for (int row = 0; row < Board.GRID_SIZE; row++)
            for (int column = 0; column < Board.GRID_SIZE; column++) {
                CellPane cellPane =  new CellPane(this);
                cellPane.setPosition(new CellPosition(row, column));
                grid[row][column] = cellPane;
                add(cellPane, column, row);
            }
    }

    public void onCellClick(CellPane cell) {
        CellPosition cellPos = cell.getPosition();
        if (board.validateMoveSelection(cellPos)) {
            board.performMove(cellPos);
            cell.setState(board.getCell(cellPos).getState());
            gameState.switchTurn();
        } else {
            System.out.println(board.getInvalidCellType());
        }
    }

    public void reset() {
        board.reset();

        for (int i = 0; i < Board.GRID_SIZE; i++) {
            List<Integer> validMoves = board.getValidRowMoves(i);
            for (int j : validMoves) {
                grid[i][j].setState(CellState.EMPTY);
            }
        }
    }

}
