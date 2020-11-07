package Morris_FX.Ui;

import Morris_FX.Logic.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class BoardPane extends GridPane implements PropertyChangeListener {
    private final ObservableBoard board;
    private final GameState gameState;
    private CellPane[][] grid;
    public BoardPane(ObservableBoard board, GameState gameState) {
        super();
        this.board = board;
        this.gameState = gameState;
        // The code below fixes the resizing issue with the game board and app window. The exact values need to be tweaked which will be done in sprint 2
        //this.setMaxSize(500,500);
        //this.setMinSize(500,500);
        setup();
    }

    public void setup() {
        this.setupBackgroundImage();
        this.setupGrid();
        this.board.addPropertyChangeListener(this);
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
                CellPane cellPane =  new CellPane(new CellPosition(row, column),this);
                grid[row][column] = cellPane;
                add(cellPane, column, row);
            }
    }

    public void onCellClick(CellPane cell) {
        CellPosition cellPos = cell.getPosition();
        if (board.validateMoveSelection(cellPos)) {
            board.performMove(cellPos);
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

    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("grid")) {
            IndexedPropertyChangeEvent indexEvent = (IndexedPropertyChangeEvent)evt;
            int index = indexEvent.getIndex();
            CellPosition position = new CellPosition(index);
            int x = position.getColumn(),y = position.getRow();
            grid[y][x].setState((CellState)evt.getNewValue());
        }
    }
}
