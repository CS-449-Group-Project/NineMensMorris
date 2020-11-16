package Morris_FX;

import Morris_FX.Logic.Board;
import Morris_FX.Logic.GameManager;
import Morris_FX.Ui.BoardPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.application.Platform;


public class Morris extends Application {

    private final GameManager gameManager;
    private final Board board;
    private final BoardPane boardPane;
    Scene scene1, scene2, scene3;

    public Morris(){
        gameManager = new GameManager();
        board = new Board(gameManager);
        boardPane = new BoardPane(board, gameManager);
    }


    public static void main(String[] args) {  launch(args); }

    @Override
    public void start(Stage primaryStage)  {

        primaryStage.setTitle("Nine Mens Morris");

        Button menu = new Button("Menu");
        Button exit = new Button("Exit");
        Button reset = new Button("Play again");

        //set exit action for the exit button
        exit.setOnAction(e -> Platform.exit());


        //creating a box for scene3 (game scene) to include the 3 above buttons
        HBox choices = new HBox();
        choices.getChildren().addAll(menu, reset, exit);


        //setting the pane for game in the window
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(boardPane);
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
            reset();
            boardPane.linkCells();
            primaryStage.setScene(scene3);
        });
        menuLabel.setFont(Font.font("Tacoma", FontWeight.NORMAL, 20));
        second.add(menuLabel, 0, 0, 2, 1);
        second.add(play, 1, 1);
        scene2 = new Scene(second, 300, 275);



        //scene 3
        // Label label3 = new Label("Game");
        reset.setOnAction(e -> reset());
        menu.setOnAction(e -> primaryStage.setScene(scene2));
        scene3 = new Scene(borderPane, 550, 600);


        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public void reset() {
        gameManager.reset();
        board.reset();
        boardPane.reset();
    }

}




