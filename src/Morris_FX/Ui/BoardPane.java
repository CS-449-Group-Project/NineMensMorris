package Morris_FX.Ui;

import Morris_FX.Logic.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class BoardPane extends GridPane {
    private final Board board;
    private final GameManager gameManager;
    private CellPane[][] grid;
    public BoardPane(Board board, GameManager gameManager) {
        super();
        this.board = board;
        this.gameManager = gameManager;
        // The code below fixes the resizing issue with the game board and app window. The exact values need to be tweaked which will be done in sprint 2
//        this.setMaxSize(500,500);
//        this.setMinSize(500,500);
        setup();
    }

    public void setup() {
        this.setupBackgroundImage();
        this.setupGrid();
    }

    private void setupBackgroundImage() {
        //create the background image from a url. Need changing the basis to the file

        FileInputStream backgroundImage;
        try{
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
            System.out.println(e);

            return;
        }


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

    // We should have this method call a different method that will have access to each players current game phase and perform
    // different actions based on the phase instead of overloading this method
    public void onCellClick(CellPane cell) {
        CellPosition cellPos = cell.getPosition();
        System.out.println(gameManager.getActivePlayer().getColor().toString());
        //if (board.validateCellSelection(cellPos)) {
        if (board.validateCellSelection(cell)) {
            //
            gameManager.performMove(cell, board);
            //cell.setState(board.getCell(cellPos).getState());
        } else {
            System.out.println(board.getInvalidCellType());
        }
    }

    public void reset() {
        // resets grid local to Board class
        board.reset();

        // resets grid local to BoardPane class; necessary for play again button
        for (int i = 0; i < Board.GRID_SIZE; i++) {
            List<Integer> validMoves = board.getValidRowMoves(i);
            for (int j : validMoves) {
                grid[i][j].setState(CellState.EMPTY);
            }
        }
    }

    CellPane findRight(CellPosition location){
        int i = location.getRow() + 1;
        int j = location.getColumn();
        for (; i < 7; i++){

            if( i == 3 && j == 3){
                return null;
            }
            if(grid[i][j].cellState == CellState.EMPTY){
                return grid[i][j];
            }
        }
        return null;
    }

    //same but left
    CellPane findLeft(CellPosition location){
        int i = location.getRow() + 1;
        int j = location.getColumn();
        for (; i >= 0; i--){

            if( i == 3 && j == 3){
                return null;
            }
            if(grid[i][j].cellState == CellState.EMPTY){
                return grid[i][j];
            }
        }
        return null;
    }

    //same but down
    CellPane findDown(CellPosition location){
        int i = location.getRow() + 1;
        int j = location.getColumn();
        for (; j < 7; j++){

            if( i == 3 && j == 3){
                return null;
            }
            if(grid[i][j].cellState == CellState.EMPTY){
                return grid[i][j];
            }
        }
        return null;
    }

    //same but up
    CellPane findUp(CellPosition location){
        int i = location.getRow() + 1;
        int j = location.getColumn();
        for (; j >= 0; j--){

            if( i == 3 && j == 3){
                return null;
            }
            if(grid[i][j].cellState == CellState.EMPTY){
                return grid[i][j];
            }
        }
        return null;
    }

    //takes all the find functions and iterates over the entire board
    //for each playable cell make a pointer to the cell up, right, down, and left of that cell
    //also add that linked cell to the list of playable cells for each cell
    //this list is used to check to find a place to move
    public void linkCells(){
        for(int j = 0; j < 7; j++) {
            for (int i = 0; i < 7; i++) {
                if(grid[i][j].cellState == CellState.EMPTY) {

                    grid[i][j].up = findUp(grid[i][j].getPosition());
                    grid[i][j].moves.add(grid[i][j].up);

                    grid[i][j].right = findRight(grid[i][j].getPosition());
                    grid[i][j].moves.add(grid[i][j].right);

                    grid[i][j].down = findDown(grid[i][j].getPosition());
                    grid[i][j].moves.add(grid[i][j].down);

                    grid[i][j].left = findLeft(grid[i][j].getPosition());
                    grid[i][j].moves.add(grid[i][j].left);
                }
            }
        }
    }

}
