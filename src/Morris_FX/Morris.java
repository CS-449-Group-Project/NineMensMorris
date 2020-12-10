package Morris_FX;

import Morris_FX.Logic.*;
import Morris_FX.Ui.BoardPane;
import Morris_FX.Ui.PiecesInHandTextField;
import Morris_FX.Ui.TurnTextField;
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

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Observable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class Morris extends Application {

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

        gameManager.onTurnSwitch((currentPlayerColor) -> {
            turnText.setText(String.format("%s's Turn", currentPlayerColor));
        });
        Player player1 = players.get(PlayerColor.BLACK);
        Player player2 = players.get(PlayerColor.WHITE);
        TurnContext turnContext = new TurnContext(player1, player2);

        turnText = new TurnTextField();
        turnContext.addPropertyChangeListener(turnText);
        gameManager.addTurnContext(turnContext);

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
        boardPane = new BoardPane(board, gameManager);
        ComputerPlayer computerPlayer = new ComputerPlayer(PlayerColor.WHITE, board, gameManager);
        gameManager.setComputerPlayer(computerPlayer);
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
    public void start(Stage primaryStage)  {

        primaryStage.setTitle("Nine Mens Morris");

        Button menu = new Button("Menu");
        Button exit = new Button("Exit");
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

        //set exit action for the exit button
        exit.setOnAction(e -> Platform.exit());

        //creating a box for scene3 (game scene) to include the 3 above buttons
        HBox choices = new HBox();
        choices.getChildren().addAll(menu, reset, exit);

        if (isDebug) {
            choices.getChildren().addAll(testGenerate, loadTestState);
        }
        VBox infoVBox = new VBox();
        HBox infoBox = new HBox();
        HBox errorBox = new HBox();

        infoVBox.getChildren().addAll(infoBox, errorBox);
        infoBox.getChildren().addAll(piecesInHandTextFields);
        infoBox.getChildren().addAll(turnText, phaseText, cellSelectedText);
        errorBox.getChildren().addAll(errorMessage, playerPiecesInHand);

        //setting the pane for game in the window
        BorderPane gameWindow = new BorderPane();
        gameWindow.setTop(infoVBox);
        gameWindow.setCenter(boardPane);
        gameWindow.setBottom(choices);

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
            primaryStage.setScene(scene3);
            // gameManager.setPlayerVersusPlayer();
            // gameManager.setPlayerVersusComputer();
        });
        menuLabel.setFont(Font.font("Tacoma", FontWeight.NORMAL, 20));
        second.add(menuLabel, 0, 0, 2, 1);
        second.add(play, 1, 1);
        scene2 = new Scene(second, 300, 275);



        //scene 3
        // Label label3 = new Label("Game");
        reset.setOnAction(e -> reset());
        menu.setOnAction(e -> primaryStage.setScene(scene2));
        scene3 = new Scene(gameWindow, 550, 675);

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