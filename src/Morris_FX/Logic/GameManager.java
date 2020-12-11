package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;
import Morris_FX.Ui.TurnTextField;
import Utils.TestFileDataGenerator;

import java.util.EnumMap;
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
    public EnumMap<phaseEnum, IPhase> phaseMap;
    private boolean playerVersusComputer;
    private ComputerPlayer computerPlayer;

    public enum phaseEnum {
        PIECE_PLACEMENT,
        PIECE_MOVEMENT,
        FLY_RULE,
        MILL_FORMED,
        GAME_OVER
    }


    public GameManager() {
        this.currentPlayer = defaultPlayer;
        setup();
    }

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

        if (currentPlayer.isMillFormed()) {
            removePieceMoves(cellPane);
            addMoves(cellPane);
            cellPane.setState(CellState.EMPTY);
            inactivePlayer.decreaseBoardPieces();
            if (!inactivePlayer.hasPiecesInHand()) {
                if (inactivePlayer.getTotalPieces() == 3) {
                    inactivePlayer.setGamePhase(Player.Phase.FLY_RULE);
                }
            }
            currentPlayer.resetMillFormed();
        } else {
            switch (currentPlayer.currentPhase) {
                case PIECE_PLACEMENT:
                    PiecePlacementPhase piecePlacementPhase = (PiecePlacementPhase) phaseMap.get(GameManager.phaseEnum.PIECE_PLACEMENT);
                    piecePlacementPhase.performMove(cellPane, currentPlayer);
                    break;
                case PIECE_MOVEMENT:
                    PieceMovementPhase pieceMovementPhase = (PieceMovementPhase) phaseMap.get(GameManager.phaseEnum.PIECE_MOVEMENT);
                    pieceMovementPhase.performMove(cellPane, currentPlayer);
                    if (currentPlayer.hasPieceToMove()) {
                        turnContext.reannounceCurrentPlayer();
                        return;
                    }
                    break;
                case FLY_RULE:
                    //add fly rule Phase
                    FlyRulePhase flyRulePhase = (FlyRulePhase) phaseMap.get(phaseEnum.FLY_RULE);
                    flyRulePhase.performMove(cellPane, currentPlayer);
                    if (currentPlayer.hasPieceToMove()) {
                        turnContext.reannounceCurrentPlayer();
                        return;
                    }
                    break;

            }
            if(MillChecker.millFormed(cellPane)){
                currentPlayer.setMillFormed();
                announcePhaseChange();
                turnContext.reannounceCurrentPlayer();
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
        switchTurn();
    }

    public void setComputerPlayer(ComputerPlayer computerPlayer) {
        this.computerPlayer = computerPlayer;
        turnContext.addPropertyChangeListener(computerPlayer);

    }

    public boolean isComputerPlayerTurn() {
        return (getPlayer() instanceof ComputerPlayer);
    }

    public void setPlayerVersusComputer() {
        playerVersusComputer = true;
    }

    public boolean getPlayerVersusComputer() {
        return playerVersusComputer;
    }

    public void setPlayerVersusPlayer() {
        playerVersusComputer = false;
    }

    public GameManager(TestFileDataGenerator testFileDataGenerator) {
        this.currentPlayer = defaultPlayer;
        this.testFileDataGenerator = testFileDataGenerator;
        setup();
    }

    private void setup() {
        if (!playerVersusComputer) {
            turnContext = new TurnContext(new Player(PlayerColor.BLACK), new Player(PlayerColor.WHITE));
        } else {
            turnContext = new TurnContext(new Player(PlayerColor.BLACK), computerPlayer);
        }

        phaseMap = new EnumMap<phaseEnum, IPhase>(phaseEnum.class);
        phaseMap.put(phaseEnum.PIECE_PLACEMENT, new PiecePlacementPhase(this));
        phaseMap.put(phaseEnum.PIECE_MOVEMENT, new PieceMovementPhase(this));
        phaseMap.put(phaseEnum.FLY_RULE, new FlyRulePhase(this));
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
            if (getPlayer().isMillFormed()) {
                phaseListener.onPhaseChange(Player.Phase.MILL_FORMED);
            } else {
                phaseListener.onPhaseChange(getPlayer().getGamePhase());
            }
        }
    }

    public interface PiecesInHandListener {
        void piecesInHandChange(int blackPieces, int whitePieces);
    }

    private PiecesInHandListener piecesInHandListener = null;
    public void onPiecesInHandChange(PiecesInHandListener listener) {
        piecesInHandListener = listener;
        announcePiecesInHandChange();
    }

    public void announcePiecesInHandChange() {
        if (piecesInHandListener != null) {
            // FIXME: should only update when currentPlayer places a piece, currently buggy
            // TODO: Have separate textboxes for each player marbles in hand
            int blackPieces = getPlayer().getPiecesInHand();
            int whitePieces = getOpponent().getPiecesInHand();
            piecesInHandListener.piecesInHandChange(blackPieces, whitePieces);
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


    public interface TurnChangeListener {
        void onTurnSwitch(PlayerColor color);
    }

    private TurnChangeListener turnChangeListener = null;

    public void onTurnSwitch(TurnChangeListener turnChangeListener) {
        this.turnChangeListener = turnChangeListener;
        // so the new listener instantly gets notified of the current player
        // turn
        announceTurnChanged();
    }

    private void announceTurnChanged() {
        if (turnChangeListener != null) {
            turnChangeListener.onTurnSwitch(getCurrentPlayerColor());
        }
    }

    public void switchTurn() {
        currentPlayer = currentPlayer.complement();
        announceTurnChanged();
        announcePhaseChange();
    }

    public void resetGameManager() {
        currentPlayer = defaultPlayer;
        announcePiecesInHandChange();
        turnContext.reset();
        turnContext.getPlayer().reset();
        turnContext.getOpponent().reset();
        millIsFormed = false;
        isGameOver = false;
        setError("");
        announceTurnChanged();
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
        Player playersPieces = getPlayer();
        if(cell.cellState == getOpponent().getPlayerColorAsCellState()) {
            playersPieces = getOpponent();
        }

        if (cell.up != null && cell.up.cellState == CellState.EMPTY){
            playersPieces.validMovesCounter--;
        }
        if (cell.down != null && cell.down.cellState == CellState.EMPTY){
            playersPieces.validMovesCounter--;
        }
        if (cell.left != null && cell.left.cellState == CellState.EMPTY){
            playersPieces.validMovesCounter--;
        }
        if (cell.right != null && cell.right.cellState == CellState.EMPTY){
            playersPieces.validMovesCounter--;
        }

    }

    //add moves for the surrounding piece when a piece is PICKED UP
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

    //removes moves for surrounding pieces when a piece is PLACED
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
