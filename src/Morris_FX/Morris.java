package Morris_FX;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.PiecesInHandTextField;
import Morris_FX.Ui.TurnTextField;
import Utils.TestCaseGenerator;
import Utils.TestFileDataGenerator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.stage.StageStyle;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Observable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class Morris extends Application {
    Player player2;
    ComputerPlayer computerOpponent;
    TurnContext turnContext;
    Player player1;

    public enum BoardOption {MARBLE, JADE, WOOD};
    public static BoardOption currentBoard = BoardOption.WOOD;
    private final GameManager gameManager;
    private final Board board;
    private final BoardPane boardPane;
    Scene scene1, scene2, scene3;

    private TurnTextField turnText;
    private TextField phaseText = new TextField();
    private TextField errorMessage = new TextField();
    private TextField cellSelectedText = new TextField();
    private TextField playerPiecesInHand = new TextField();
    private PiecesInHandTextField[] piecesInHandTextFields = new PiecesInHandTextField[2];
    private TestFileDataGenerator testFileData;
    private boolean isDebug;

    public Morris(){
        // requires intellij to be running in debug mode
        isDebug = java.lang.management.ManagementFactory.
                getRuntimeMXBean().
                getInputArguments().toString().contains("jdwp");
        HashMap<PlayerColor, Player> players = new HashMap<>();
        int i = 0;
        for (PlayerColor color: PlayerColor.values()) {
            Player currentPlayer = new Player(color);
            players.put(color, currentPlayer);
            piecesInHandTextFields[i] = new PiecesInHandTextField(color);
            currentPlayer.addPropertyChangeListener(piecesInHandTextFields[i]);
            i++;
        }

        if (isDebug) {
            testFileData = new TestFileDataGenerator(Board.GRID_SIZE);
            gameManager = new GameManager(testFileData);
        } else {
            gameManager = new GameManager();
        }



        // these needs to be update when individual phases separated
        gameManager.onCellSelected((piece) -> {
            if (piece != null) {
                cellSelectedText.setText(String.format("Selected %s", piece.getPosition()));
            } else {
                cellSelectedText.setText("");
            }

        });

        gameManager.onPhaseChange(phase -> {
            phaseText.setText(phase.toString());
        });

        gameManager.onError(errorMsg -> {
            errorMessage.setText(errorMsg);
        });

        gameManager.onPiecesInHandChange((blackPieces, whitePieces) -> {
            playerPiecesInHand.setText(String.format("BLACK pieces: %d, WHITE pieces: %d", blackPieces, whitePieces));
        });

        board = new Board(gameManager, true);

        //gameManager.setComputerPlayer(computerPlayer);
        // Player player2 = players.get(PlayerColor.WHITE);
        //gameManager.setPlayerVersusComputer();

        player1 = players.get(PlayerColor.BLACK);
        player2 = players.get(PlayerColor.WHITE);
        computerOpponent = new ComputerPlayer(PlayerColor.WHITE, board, gameManager);


        turnText = new TurnTextField();


        boardPane = new BoardPane(board, gameManager);
        boardPane.setPadding(new Insets((30), 0, 20, 35));

        phaseText.setMaxWidth(150);
        phaseText.setDisable(true);
        phaseText.setStyle("-fx-opacity: 1;");
        errorMessage.setMaxWidth(200);
        errorMessage.setDisable(true);
        errorMessage.setStyle("-fx-opacity: 1;");
        cellSelectedText.setMaxWidth(100);
        cellSelectedText.setDisable(true);
        cellSelectedText.setStyle("-fx-opacity: 1;");

        playerPiecesInHand.setMinWidth(275);
        playerPiecesInHand.setDisable(true);
        playerPiecesInHand.setStyle("-fx-opacity: 1;");
    }


    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        TurnContext turnContextHuman = new TurnContext(player1, player2);
        TurnContext turnContextComputer = new TurnContext(player1, computerOpponent);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Nine Mens Morris");

        Image gear = new Image(new FileInputStream("./images/gear_Icon.png"), 35,35,false,true);
        ImageView gearIcon = new ImageView(gear);
        Image one = new Image(new FileInputStream("./images/1P_Icon.png"), 35,35,false,true);
        ImageView onePlayerIcon = new ImageView(one);
        Image two = new Image(new FileInputStream("./images/2P_Icon.png"), 37,37,false,true);
        ImageView twoPlayerIcon = new ImageView(two);
        Image marbles = new Image(new FileInputStream("./images/2Marbles.png"), 200,200,false, true);
        ImageView twoMarbles = new ImageView(marbles);


        Button twoPlayer = new Button("    TWO\n PLAYERS");
        twoPlayer.setId("twoPlayer");
        twoPlayer.setGraphic(twoPlayerIcon);
        twoPlayer.setLayoutY(365);
        twoPlayer.setLayoutX(25);
        twoPlayer.setMinSize(100,70);
        twoPlayer.setOnAction(e -> {
            gameManager.addTurnContext(turnContextHuman);
            primaryStage.setScene(scene3);

        });

        Button Ai = new Button("SINGLE \nPLAYER");
        Ai.setGraphic(onePlayerIcon);
        Ai.setLayoutY(365);
        Ai.setLayoutX(155);
        Ai.setMinSize(100,70);
        Ai.setOnAction(e -> {
            gameManager.setPlayerVersusComputer();
            gameManager.addTurnContext(turnContextComputer);
            turnContextComputer.addPropertyChangeListener(computerOpponent);
            primaryStage.setScene(scene3);
        });

        Button gameMenu = new Button("Menu");
        gameMenu.setLayoutX(110);
        gameMenu.setOnAction(e -> {
            primaryStage.setScene(scene2);
        });
        Button reset = new Button("Play again");

        // https://stackoverflow.com/a/28754689
        Button testGenerate = new Button("Save State.");
        Button loadTestState = new Button("Load save state.");
        if (isDebug) {
            testGenerate.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TEST INPUT DATA", "*.td"));

                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    boolean succeeded = testFileData.generateFile(file.getAbsolutePath());
                    if (succeeded) {
                        String filePath = file.getAbsolutePath();
                        System.out.println(String.format("You can find the file at:%s", filePath));
                    } else {
                        System.out.println(String.format("Failed to create test case file."));
                    }
                } else  {
                    System.out.println("error"); // or something else
                }

            });

            loadTestState.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("TEST INPUT DATA", "*.td")
                );

                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    TestCaseGenerator testCaseObject;
                    try {
                        testCaseObject = TestCaseGenerator.createFromFile(file.getAbsolutePath());
                        reset();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        return;
                    }
                    Thread t = new Thread(() -> {
                        loadTestState.setDisable(true);
                        boardPane.setDisable(true);
                        for (CellPosition recordedPos: testCaseObject) {
                            gameManager.performMove(board.getCell(recordedPos));
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                        boardPane.setDisable(false);
                        loadTestState.setDisable(false);
                    });
                    t.start();
                }
            });
        }


        //creating a box for scene3 (game scene) to include the 3 above buttons

        VBox infoVBox = new VBox();
        infoVBox.setId("box");
        HBox infoBox = new HBox();
        infoBox.setId("box");
        HBox errorBox = new HBox();
        errorBox.setId("box");
        turnText.setId("box");
        phaseText.setId("box");
        cellSelectedText.setId("box");
        errorMessage.setId("box");
        playerPiecesInHand.setId("box");

        infoVBox.getChildren().addAll(infoBox, errorBox);
        infoBox.getChildren().addAll(piecesInHandTextFields);
        infoBox.getChildren().addAll(turnText, phaseText, cellSelectedText);
        errorBox.getChildren().addAll(errorMessage, playerPiecesInHand);

        Pane topBar = new Pane();
        Pane gameTopBar = new Pane();
        topBar.setId("topBar");
        gameTopBar.setId("topBar");
        topBar.setMinSize(550, 30);
        gameTopBar.setMinSize(550, 30);
        gameTopBar.setOnMousePressed(pressEvent -> {
            gameTopBar.setOnMouseDragged(dragEvent -> {
                primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });

        if (isDebug) {
            topBar.getChildren().addAll(testGenerate, loadTestState);
        }

        Button menu = new Button();
        menu.setGraphic(gearIcon);
        menu.setLayoutY(365);
        menu.setLayoutX(275);
        menu.setMinSize(100,70);
        menu.setOnAction(e -> {
            primaryStage.setScene(scene2);
        });

        Button exit = new Button("X");
        exit.setId("X");
        exit.setMinSize(25, 25);
        //exit.setLayoutY(15);
        exit.setLayoutX(520);
        exit.setOnAction(e -> Platform.exit());

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
        gameTopBar.getChildren().addAll( gameMenu, reset, minimize, exit);

        //setting the pane for game in the window
        BorderPane gameWindow = new BorderPane();
        gameWindow.setId("gameWindow");
        gameWindow.setTop(gameTopBar);
        gameWindow.setCenter(boardPane);
        gameWindow.setBottom(infoVBox);


//SCENE 1


        twoMarbles.setLayoutX(325);
        twoMarbles.setLayoutY(170);

        Pane first = new Pane();
        first.setId("firstPane");


        Pane firstTitle = new Pane();
        firstTitle.setMinSize(550, 500);
        firstTitle.setLayoutY(30);
        firstTitle.setId("firstTitle");



        Label title = new Label(" NINE\n MENS\n MORRIS");
        title.setMaxSize(350,500);
        title.setLayoutY(35);
        title.setLayoutX(20);

        firstTitle.getChildren().addAll(title, Ai, twoPlayer, menu);
        first.getChildren().addAll(topBar, firstTitle, twoMarbles);

        scene1 = new Scene(first, 550,600);
        scene1.setFill(Color.TRANSPARENT);
        scene1.getStylesheets().add(Morris.class.getResource("StageDesign.css").toExternalForm());

        minimize.setLayoutX(490);
        minimize.setOnAction(e -> {
            ((Stage)((Button)e.getSource()).getScene().getWindow()).setIconified(true);
        });
        topBar.getChildren().addAll( menu, reset, minimize, exit);

        reset.setOnAction(e -> reset());
        //menu.setOnAction(e -> primaryStage.setScene(scene2));
        scene3 = new Scene(gameWindow, 550, 675);
        scene3.getStylesheets().add(Morris.class.getResource("StageDesign.css").toExternalForm());
        scene2 = SceneBuilder.createMenuScene(primaryStage, scene3, boardPane, gameManager);


        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public void reset() {
        gameManager.resetGameManager();
        board.reset();
        gameManager.getPlayer().reset();
        gameManager.getOpponent().reset();
    }

}