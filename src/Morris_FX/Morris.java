package Morris_FX;

import Morris_FX.Board.Board;
import Morris_FX.Board.CellUi;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Skin;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.effect.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Ellipse;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import Morris_FX.Board.Board;


public class Morris extends Application {


    private Board board;
    private CellUi[][] uiGrid;
    public Morris() {
        super();
        this.board = new Board();
        uiGrid = new CellUi[Board.GRID_SIZE][Board.GRID_SIZE];
    }


    Scene scene1, scene2, scene3;

    public static void main(String[] args) {  launch(args); }

    @Override
    public void start(Stage primaryStage)  {

        primaryStage.setTitle("Nine Mens Morris");

        Button menu = new Button("Menu");
        Button exit = new Button("Exit");
        Button again = new Button("Play again");

//set exit action for the exit button
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Platform.exit();
            }
        });

//create the background image from a url. Need changing the basis to the file
        BackgroundImage emptyBoard= new BackgroundImage(new Image("https://www.iconfinder.com/data/icons/toys-2/512/game-6-512.png",550,450,false,true),
                BackgroundRepeat.NO_REPEAT.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

//creating a box for scene3 (game scene) to include the 3 above buttons
        HBox choices = new HBox();
        choices.getChildren().addAll(menu, again, exit);

//create grid pane for the game and filling with 7x7 cells
        GridPane pane = new GridPane();
        pane.setBackground(new Background(emptyBoard));
        for (int i = 0; i < Board.GRID_SIZE; i++)
            for (int j = 0; j < Board.GRID_SIZE; j++)
                pane.add(uiGrid[i][j] = new CellUi(board.getCell(i, j)), j, i);
        resetNodes();
//setting the pane for game in the window
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        borderPane.setBottom(choices);

//Scene 1
        GridPane first = new GridPane();
        first.setAlignment(Pos.CENTER);
        first.setHgap(10);
        first.setVgap(10);
        first.setPadding(new Insets((25), 25, 25, 25));

        Text welcome = new Text ("Welcome");
        Button enter = new Button("Enter");
        enter.setOnAction(e -> primaryStage.setScene(scene2));

        welcome.setFont(Font.font("Tacoma", FontWeight.NORMAL, 20));
        first.add(welcome, 0, 0, 2, 1);
        first.add(enter, 1, 1);

        scene1 = new Scene(first, 300, 275);

//Scene 2
        GridPane second = new GridPane();
        second.setAlignment(Pos.CENTER);
        second.setHgap(10);
        second.setVgap(10);
        second.setPadding(new Insets((25), 25, 25, 25));

        Text menuLabel = new Text("Menu");
        Button play = new Button("Play");
        play.setOnAction(e -> {
            resetNodes();
            primaryStage.setScene(scene3);
        });
        menuLabel.setFont(Font.font("Tacoma", FontWeight.NORMAL, 20));
        second.add(menuLabel, 0, 0, 2, 1);
        second.add(play, 1, 1);
        scene2 = new Scene(second, 300, 275);



//scene 3
        Label label3 = new Label("Game");
        again.setOnAction(e -> resetNodes());
        menu.setOnAction(e -> primaryStage.setScene(scene2));
        scene3 = new Scene(borderPane, 550, 470);


        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    private void resetNodes() {
        for(int i = 0; i < Board.GRID_SIZE; i++) {
            for (int j = 0; j < Board.GRID_SIZE; j++) {
                uiGrid[i][j].reset();
            }
        }
        board.reset();
    }
}




