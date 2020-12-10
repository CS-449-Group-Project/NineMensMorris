package Morris_FX;

import Morris_FX.Logic.Board;
import Morris_FX.Logic.CellPosition;
import Morris_FX.Logic.GameManager;
import Morris_FX.Ui.BoardPane;
import Utils.TestCaseGenerator;
import Utils.TestFileDataGenerator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class Morris extends Application {

    public enum BoardOption {MARBLE, JADE, WOOD};
    public static BoardOption currentBoard = BoardOption.WOOD;
    private final GameManager gameManager;
    private final Board board;
    private final BoardPane boardPane;
    Scene scene1, scene2, scene3;

    private TextField turnText = new TextField();
    private TextField phaseText = new TextField();
    private TextField errorMessage = new TextField();
    private TextField cellSelectedText = new TextField();
    private TextField playerPiecesInHand = new TextField();
    private TestFileDataGenerator testFileData;
    private boolean isDebug;

    public Morris(){
        // requires intellij to be running in debug mode
        isDebug = java.lang.management.ManagementFactory.
                getRuntimeMXBean().
                getInputArguments().toString().contains("jdwp");
        if (isDebug) {
            testFileData = new TestFileDataGenerator(Board.GRID_SIZE);
            gameManager = new GameManager(testFileData);
        } else {
            gameManager = new GameManager();
        }

        gameManager.onTurnSwitch((currentPlayerColor) -> {
            turnText.setText(String.format("%s's Turn", currentPlayerColor));
        });

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
        boardPane = new BoardPane(board, gameManager);
        boardPane.setPadding(new Insets((30), 0, 20, 35));

        turnText.setMaxWidth(120);
        turnText.setDisable(true);
        turnText.setStyle("-fx-opacity: 1;");
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

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Nine Mens Morris");

        Button menu = new Button("Menu");
        menu.setLayoutX(110);
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
        infoBox.getChildren().addAll(turnText,phaseText, cellSelectedText);
        errorBox.getChildren().addAll(errorMessage, playerPiecesInHand);

        Pane topBar = new Pane();
        topBar.setId("topBar");
        topBar.setMinSize(550, 30);

        if (isDebug) {
            topBar.getChildren().addAll(testGenerate, loadTestState);
        }

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
        topBar.getChildren().addAll( menu, reset, minimize, exit);

        //setting the pane for game in the window
        BorderPane gameWindow = new BorderPane();
        gameWindow.setId("gameWindow");
        gameWindow.setTop(topBar);
        gameWindow.setCenter(boardPane);
        gameWindow.setBottom(infoVBox);

        //Scene 1
        reset.setOnAction(e -> reset());
        menu.setOnAction(e -> primaryStage.setScene(scene2));
        scene3 = new Scene(gameWindow, 550, 675);
        scene3.getStylesheets().add(Morris.class.getResource("StageDesign.css").toExternalForm());
        scene2 = SceneBuilder.createMenuScene(primaryStage, scene3, boardPane);
        scene1 = SceneBuilder.createFirstScene(primaryStage, scene2, scene3);

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




