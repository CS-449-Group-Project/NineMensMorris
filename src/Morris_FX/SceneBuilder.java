package Morris_FX;

import Morris_FX.Logic.GameManager;
import Morris_FX.Ui.BoardPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.beans.value.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SceneBuilder {

    public static Scene createFirstScene(Stage primaryStage, Scene menuScene, Scene gameScene, GameManager gameManager) throws FileNotFoundException {
        Image gear = new Image(new FileInputStream("./images/gear_Icon.png"), 35,35,false,true);
        ImageView gearIcon = new ImageView(gear);
        Image one = new Image(new FileInputStream("./images/1P_Icon.png"), 35,35,false,true);
        ImageView onePlayerIcon = new ImageView(one);
        Image two = new Image(new FileInputStream("./images/2P_Icon.png"), 37,37,false,true);
        ImageView twoPlayerIcon = new ImageView(two);
        Image marbles = new Image(new FileInputStream("./images/2Marbles.png"), 200,200,false, true);
        ImageView twoMarbles = new ImageView(marbles);
        twoMarbles.setLayoutX(325);
        twoMarbles.setLayoutY(170);

        Pane first = new Pane();
        first.setId("firstPane");

        Button twoPlayer = new Button("    TWO\n PLAYERS");
        twoPlayer.setId("twoPlayer");
        twoPlayer.setGraphic(twoPlayerIcon);
        twoPlayer.setLayoutY(365);
        twoPlayer.setLayoutX(25);
        twoPlayer.setMinSize(100,70);
        twoPlayer.setOnAction(e -> {
            primaryStage.setScene(gameScene);
        });

        Button Ai = new Button("SINGLE \nPLAYER");
        Ai.setGraphic(onePlayerIcon);
        Ai.setLayoutY(365);
        Ai.setLayoutX(155);
        Ai.setMinSize(100,70);
        Ai.setOnAction(e -> {
            gameManager.setPlayerVersusComputer();
            primaryStage.setScene(gameScene);
        });

        Button menu = new Button();
        menu.setGraphic(gearIcon);
        menu.setLayoutY(365);
        menu.setLayoutX(275);
        menu.setMinSize(100,70);
        menu.setOnAction(e -> {
            primaryStage.setScene(menuScene);
        });

        Pane firstTitle = new Pane();
        firstTitle.setMinSize(550, 500);
        firstTitle.setLayoutY(30);
        firstTitle.setId("firstTitle");

        Pane topBar = new Pane();
        topBar.setId("topBar");
        topBar.setMinSize(550, 30);

        Button exit = new Button("X");
        exit.setId("X");
        exit.setMinSize(25, 25);
        //exit.setLayoutY(15);
        exit.setLayoutX(520);
        exit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent){
                Platform.exit();
            }
        });

        topBar.setOnMousePressed(pressEvent -> {
            topBar.setOnMouseDragged(dragEvent -> {
                primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

        Button minimize = new Button("-");
        minimize.setId("minimize");
        minimize.setMinSize(25,30);

        minimize.setLayoutX(490);
        minimize.setOnAction(e -> {
            ((Stage)((Button)e.getSource()).getScene().getWindow()).setIconified(true);
        });
        topBar.getChildren().addAll( minimize, exit);

        Label title = new Label(" NINE\n MENS\n MORRIS");
        title.setMaxSize(350,500);
        title.setLayoutY(35);
        title.setLayoutX(20);

        firstTitle.getChildren().addAll(title, Ai, twoPlayer, menu);
        first.getChildren().addAll(topBar, firstTitle, twoMarbles);

        Scene returnValue = new Scene(first, 550,600);
        returnValue.setFill(Color.TRANSPARENT);
        returnValue.getStylesheets().add(Morris.class.getResource("StageDesign.css").toExternalForm());
        return returnValue;
    }


    public static Scene createMenuScene(Stage primaryStage, Scene gameScene, BoardPane boardPane, GameManager gameManager) throws FileNotFoundException {
        Image gear = new Image(new FileInputStream("./images/gear_Icon.png"), 45,45,false,true);
        ImageView gearIcon = new ImageView(gear);
        Image one = new Image(new FileInputStream("./images/1P_Icon.png"), 50,50,false,true);
        ImageView onePlayerIcon = new ImageView(one);
        Image two = new Image(new FileInputStream("./images/2P_Icon.png"), 50,50,false,true);
        ImageView twoPlayerIcon = new ImageView(two);

        ToggleGroup board = new ToggleGroup();
        RadioButton wood = new RadioButton("Wooden Board");
        wood.setId("toggle");
        wood.setLayoutX(30);
        wood.setLayoutY(75);
        RadioButton jade = new RadioButton("Jade Board");
        jade.setId("toggle");
        jade.setLayoutX(30);
        jade.setLayoutY(125);
        RadioButton marble = new RadioButton("Marble board");
        marble.setId("toggle");
        marble.setLayoutX(30);
        marble.setLayoutY(175);
        wood.setToggleGroup(board);
        wood.setSelected(true);
        jade.setToggleGroup(board);
        marble.setToggleGroup(board);

        board.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (board.getSelectedToggle() != null) {
                    if (board.getSelectedToggle() == wood){
                        Morris.currentBoard = Morris.BoardOption.WOOD;
                    }
                    if (board.getSelectedToggle() == jade){
                        Morris.currentBoard = Morris.BoardOption.JADE;
                        System.out.println("JADE\n");
                    }
                    if (board.getSelectedToggle() == marble){
                        Morris.currentBoard = Morris.BoardOption.MARBLE;
                        System.out.println("MARBLE\n");
                    }
                }
            }
        });

        Pane second = new Pane();
        second.setId("firstPane");

        Button twoPlayer = new Button("    PLAY");
        twoPlayer.setId("twoPlayer");
        twoPlayer.setGraphic(twoPlayerIcon);
        twoPlayer.setLayoutY(365);
        twoPlayer.setLayoutX(25);
        twoPlayer.setMinSize(100,70);
        twoPlayer.setOnAction(e -> {
            boardPane.setupBackgroundImage();
            primaryStage.setScene(gameScene);
        });

//        Button Ai = new Button("SINGLE \nPLAYER");
//        Ai.setGraphic(onePlayerIcon);
//        Ai.setLayoutY(365);
//        Ai.setLayoutX(155);
//        Ai.setMinSize(100,70);
//        Ai.setOnAction(e -> {
//            boardPane.setupBackgroundImage();
//            gameManager.setPlayerVersusComputer();
//            primaryStage.setScene(gameScene);
//        });

        Pane firstTitle = new Pane();
        firstTitle.setMinSize(550, 500);
        firstTitle.setLayoutY(30);
        firstTitle.setId("firstTitle");

        Pane topBar = new Pane();
        topBar.setId("topBar");
        topBar.setMinSize(550, 30);

        Button exit = new Button("X");
        exit.setId("X");
        exit.setMinSize(25, 25);
        //exit.setLayoutY(15);
        exit.setLayoutX(520);
        exit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent){
                Platform.exit();
            }
        });

        topBar.setOnMousePressed(pressEvent -> {
            topBar.setOnMouseDragged(dragEvent -> {
                primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

        Button minimize = new Button("-");
        minimize.setId("minimize");
        minimize.setMinSize(25,30);

        minimize.setLayoutX(490);
        minimize.setOnAction(e -> {
            ((Stage)((Button)e.getSource()).getScene().getWindow()).setIconified(true);
        });
        topBar.getChildren().addAll( minimize, exit);

        firstTitle.getChildren().addAll(wood, jade, marble, twoPlayer);
        second.getChildren().addAll(topBar, firstTitle);

        Scene returnValue = new Scene(second, 550,600);
        returnValue.setFill(Color.TRANSPARENT);
        returnValue.getStylesheets().add(Morris.class.getResource("StageDesign.css").toExternalForm());
        return returnValue;
    }
}
