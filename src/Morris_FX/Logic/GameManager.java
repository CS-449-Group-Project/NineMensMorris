package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;
import Morris_FX.Ui.TurnTextField;
import Utils.TestFileDataGenerator;

import java.beans.PropertyChangeListener;
import java.util.Vector;

public class GameManager {
    private TestFileDataGenerator testFileDataGenerator;
    public Vector<CellPosition> allPlacedPieces = new Vector<>(50);
    public Vector<String> piecePlacementComments = new Vector<>(50);
    // delet this
    private TurnContext turnContext;
    // change from millIsFormed to isMill?
    private boolean millIsFormed = false;
    private boolean isGameOver = false;
    private PlayerColor defaultPlayer = PlayerColor.BLACK;
    private PlayerColor currentPlayer;

    public static GameManager create() {
        GameManager gameManager = new GameManager();
        TurnContext context = new TurnContext(new Player(PlayerColor.BLACK), new Player(PlayerColor.WHITE));
        gameManager.addTurnContext(context);
        return gameManager;
    }

    // for PIECE_PLACEMENT phase this method sets the state of the clicked cell equal to the player color; "Places current
    // player piece on the board"
    // for PIECE_MOVEMENT phase this method sets the pieceToMove variable of the current player to the clicked cell if they
    // dont have one to move already; "Selects a piece"
    // if they do have a piece to move already then it sets the state of the clicked cell to the player color, sets the
    // previously occupied cell to empty, and set pieceToMove to null in the current player object; "Places pieceToMove in new position"
    public void performMove(CellPane cellPane) {
        Player currentPlayer = turnContext.getPlayer();
        Player inactivePlayer = turnContext.getOpponent();

        boolean isTesting = testFileDataGenerator != null;
        if (isTesting) {
            testFileDataGenerator.addPosition(cellPane.getPosition());
        }

        if (isMillFormed()) {
            removePieceMoves(cellPane);
            addMoves(cellPane);
            cellPane.setState(CellState.EMPTY);
            inactivePlayer.decreaseBoardPieces();
            if (!inactivePlayer.hasPiecesInHand()) {
                if (inactivePlayer.getTotalPieces() == 3) {
                    inactivePlayer.setGamePhase(Player.Phase.FLY_RULE);
                }
            }
            resetMill();
        } else {
            switch (currentPlayer.currentPhase) {
                case PIECE_PLACEMENT:
                    currentPlayer.removePiecesFromHand();
                    cellPane.setState(currentPlayer.getPlayerColorAsCellState());
                    getPlayer().increaseBoardPieces();
                    addPlacedPieceMoves(cellPane);
                    removeMoves(cellPane);

                    if (!currentPlayer.hasPiecesInHand()) {
                        if (currentPlayer.getTotalPieces() == 3) {
                            currentPlayer.setGamePhase(Player.Phase.FLY_RULE);
                        } else {
                            currentPlayer.setGamePhase(Player.Phase.PIECE_MOVEMENT);
                        }

                    }
                    break;
                case PIECE_MOVEMENT:
                    if (!currentPlayer.hasPieceToMove()) {
                        setCellSelect(cellPane);
                        currentPlayer.setPieceToMove(cellPane);
                        return;
                    }
                    cellPane.setState(currentPlayer.getPlayerColorAsCellState());
                    addPlacedPieceMoves(cellPane);
                    removeMoves(cellPane);
                    currentPlayer.pieceToMove.setState(CellState.EMPTY);
                    addMoves(currentPlayer.pieceToMove);
                    removePieceMoves(currentPlayer.pieceToMove);
                    currentPlayer.removePieceToMove();
                    setCellSelect(null);
                    break;
                case FLY_RULE:
                    if (!currentPlayer.hasPieceToMove()) {
                        setCellSelect(cellPane);
                        currentPlayer.setPieceToMove(cellPane);
                        return;
                    }
                    setCellSelect(null);
                    cellPane.setState(currentPlayer.getPlayerColorAsCellState());
                    removeMoves(cellPane);
                    currentPlayer.pieceToMove.setState(CellState.EMPTY);
                    addMoves(currentPlayer.pieceToMove);
                    removePieceMoves(currentPlayer.pieceToMove);
                    currentPlayer.removePieceToMove();
                    break;
            }
            if(millFormed(cellPane)){
                return;
            }
        }
        isGameOver = inactivePlayer.getTotalPieces() == 2;
        if (inactivePlayer.getGamePhase() != Player.Phase.PIECE_PLACEMENT) {
            Board board = cellPane.getParentPane().getBoard();
            int validMovesCount = board.getValidMoveCount(getOpponentCellState());
            // check if
             isGameOver = isGameOver || validMovesCount == 0;
        }


        if (isGameOver) {
            getPlayer().setGamePhase(Player.Phase.GAME_OVER);
            setError(getCurrentPlayerColor() + " won!");
            return;
        }
        turnContext.switchPlayers();
    }

    public GameManager() {
        this.currentPlayer = defaultPlayer;
    }

    public GameManager(TestFileDataGenerator testFileDataGenerator) {
        this.currentPlayer = defaultPlayer;
        this.testFileDataGenerator = testFileDataGenerator;
    }

    public PlayerColor getCurrentPlayerColor() {
        return getPlayer().getColor();
    }

    public Player getPlayer() {
        return turnContext.getPlayer();
    }

    public Player getOpponent() {
        return turnContext.getOpponent();
    }

    public CellState getOpponentCellState() {return turnContext.getOpponent().getPlayerColorAsCellState();}

    public void resetMill(){
        this.millIsFormed = false;
    }

    public void setMillIsFormedToTrue() {
        this.millIsFormed = true;
        announcePhaseChange();
    }

    // change from isMillFormed to millIsFormed()?
    public boolean isMillFormed() {
        return this.millIsFormed;
    }

    public boolean millFormed (CellPane cell){
        CellPane recursiveCell = cell;
        int vertical = 1;
        int horizontal = 1;

        //check vertical first, check up pointer until either the reference is null or the playstate is not the 'color' of the given cell
        while(recursiveCell.up != null && recursiveCell.up.cellState == cell.cellState) {
            recursiveCell = recursiveCell.up;
            vertical++;
        }
        recursiveCell = cell;
        while(recursiveCell.down != null && recursiveCell.down.cellState == cell.cellState) {
            recursiveCell = recursiveCell.down;
            vertical++;
        }
        if (vertical == 3) {
            setMillIsFormedToTrue();
            return true;
        }
        recursiveCell = cell;

        //now check horizontal
        while(recursiveCell.left != null && recursiveCell.left.cellState == cell.cellState) {
            recursiveCell = recursiveCell.left;
            horizontal++;
        }
        recursiveCell = cell;
        while(recursiveCell.right != null && recursiveCell.right.cellState == cell.cellState) {
            recursiveCell = recursiveCell.right;
            horizontal++;
        }
        if (horizontal == 3) {
            setMillIsFormedToTrue();
            return true;
        }

        return false;
    }

    public boolean isOver() {
        return isGameOver;
    }

    public void addTurnContext(TurnContext turnContext) {
        this.turnContext = turnContext;
    }

    public interface OnPhaseChangeListener {
        void onPhaseChange(Player.Phase phase);
    }

    private OnPhaseChangeListener phaseListener = null;
    public void onPhaseChange(OnPhaseChangeListener listener) {
        phaseListener = listener;
        announcePhaseChange();
    }

    public void announcePhaseChange() {
        if (phaseListener != null) {
            if (isMillFormed()) {
                phaseListener.onPhaseChange(Player.Phase.MILL_FORMED);
            } else {
                phaseListener.onPhaseChange(getPlayer().getGamePhase());
            }

        }
    }


    public interface CellSelectListener {
        void onCellSelect(CellPane cell);
    }

    private CellSelectListener cellSelectListener = null;

    public void onCellSelected(CellSelectListener cellSelectListener) {
        this.cellSelectListener = cellSelectListener;
        announceCellSelectionUpdate();
    }

    CellPane selectedCell = null;
    public void setCellSelect(CellPane cellPane) {
        selectedCell = cellPane;
        announceCellSelectionUpdate();
    }

    public void announceCellSelectionUpdate() {
        if (cellSelectListener != null) {
            cellSelectListener.onCellSelect(selectedCell);
        }
    }

    public interface ErrorListener {
        void onError(String errorMsg);
    }

    private ErrorListener errorListener = null;
    private String errorMsg = "";

    public void onError(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void setError(String msg) {
        errorMsg = msg;
        announceError();
    }

    public void announceError() {
        if (errorListener != null) {
            errorListener.onError(errorMsg);
        }
    }


    public void resetGameManager() {
        currentPlayer = defaultPlayer;
        turnContext.reset();
        turnContext.getPlayer().reset();
        turnContext.getOpponent().reset();
        millIsFormed = false;
        isGameOver = false;
        setError("");
        announcePhaseChange();
        allPlacedPieces.clear();
        if (testFileDataGenerator != null) {
            testFileDataGenerator.reset();
        }
    }

    public void addPlacedPieceMoves(CellPane cell){
        if (cell.up != null && cell.up.cellState == CellState.EMPTY){
            getPlayer().validMovesCounter++;
        }
        if (cell.down != null && cell.down.cellState == CellState.EMPTY){
            getPlayer().validMovesCounter++;
        }
        if (cell.left != null && cell.left.cellState == CellState.EMPTY){
            getPlayer().validMovesCounter++;
        }
        if (cell.right != null && cell.right.cellState == CellState.EMPTY){
            getPlayer().validMovesCounter++;
        }

    }

    public void removePieceMoves(CellPane cell){
        Player playersMarble = getPlayer();
        if(cell.cellState == getOpponent().getPlayerColorAsCellState()) {
            playersMarble = getOpponent();
        }

        if (cell.up != null && cell.up.cellState == CellState.EMPTY){
            playersMarble.validMovesCounter--;
        }
        if (cell.down != null && cell.down.cellState == CellState.EMPTY){
            playersMarble.validMovesCounter--;
        }
        if (cell.left != null && cell.left.cellState == CellState.EMPTY){
            playersMarble.validMovesCounter--;
        }
        if (cell.right != null && cell.right.cellState == CellState.EMPTY){
            playersMarble.validMovesCounter--;
        }

    }

    //add moves for the surrounding marble when a piece is PICKED UP
    public void addMoves(CellPane cell){
        if (cell.up!= null && cell.up.cellState == getPlayer().getPlayerColorAsCellState()){
            getPlayer().validMovesCounter++;
        }
        if (cell.up != null && cell.up.cellState == getOpponent().getPlayerColorAsCellState()){
            getOpponent().validMovesCounter++;
        }
        if (cell.down != null && cell.down.cellState == getPlayer().getPlayerColorAsCellState()){
            getPlayer().validMovesCounter++;
        }
        if (cell.down != null && cell.down.cellState == getOpponent().getPlayerColorAsCellState()){
            getOpponent().validMovesCounter++;
        }
        if (cell.left != null && cell.left.cellState == getPlayer().getPlayerColorAsCellState()){
            getPlayer().validMovesCounter++;
        }
        if (cell.left != null && cell.left.cellState == getOpponent().getPlayerColorAsCellState()){
            getOpponent().validMovesCounter++;
        }
        if (cell.right != null && cell.right.cellState == getPlayer().getPlayerColorAsCellState()){
            getPlayer().validMovesCounter++;
        }
        if (cell.right != null && cell.right.cellState == getOpponent().getPlayerColorAsCellState()){
            getOpponent().validMovesCounter++;
        }
    }

    //removes moves for surrounding marbles when a piece is PLACED
    public void removeMoves(CellPane cell){
        if (cell.up != null && cell.up.cellState == getPlayer().getPlayerColorAsCellState()){
            getPlayer().validMovesCounter--;
        }
        if (cell.up != null && cell.up.cellState == getOpponent().getPlayerColorAsCellState()){
            getOpponent().validMovesCounter--;
        }
        if (cell.down != null && cell.down.cellState == getPlayer().getPlayerColorAsCellState()){
            getPlayer().validMovesCounter--;
        }
        if (cell.down != null && cell.down.cellState == getOpponent().getPlayerColorAsCellState()){
            getOpponent().validMovesCounter--;
        }
        if (cell.left != null && cell.left.cellState == getPlayer().getPlayerColorAsCellState()){
            getPlayer().validMovesCounter--;
        }
        if (cell.left != null && cell.left.cellState == getOpponent().getPlayerColorAsCellState()){
            getOpponent().validMovesCounter--;
        }
        if (cell.right != null && cell.right.cellState == getPlayer().getPlayerColorAsCellState()){
            getPlayer().validMovesCounter--;
        }
        if (cell.right != null && cell.right.cellState == getOpponent().getPlayerColorAsCellState()){
            getOpponent().validMovesCounter--;
        }
    }
}
