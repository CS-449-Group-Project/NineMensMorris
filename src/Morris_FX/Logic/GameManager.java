package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;
import Utils.TestFileDataGenerator;

import java.util.EnumMap;
import java.util.Map;
import java.util.Vector;

public class GameManager {
    private TestFileDataGenerator testFileDataGenerator;
    public Vector<CellPosition> allPlacedPieces = new Vector<>(50);
    public Vector<String> piecePlacementComments = new Vector<>(50);
    private Map<PlayerColor,Player> player;
    private boolean millIsFormed = false;
    private boolean isGameOver = false;
    private PlayerColor defaultPlayer = PlayerColor.BLACK;
    private PlayerColor currentPlayer;
    public EnumMap<phaseEnum, IPhase> phaseMap;

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


    public void performMove(CellPane cellPane) {
        Player currentPlayer = getCurrentPlayer();
        Player inactivePlayer = getInactivePlayer();

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
                    PiecePlacementPhase piecePlacementPhase = (PiecePlacementPhase) phaseMap.get(GameManager.phaseEnum.PIECE_PLACEMENT);
                    piecePlacementPhase.performMove(cellPane, currentPlayer);
                    break;
                case PIECE_MOVEMENT:
                    PieceMovementPhase pieceMovementPhase = (PieceMovementPhase) phaseMap.get(GameManager.phaseEnum.PIECE_MOVEMENT);
                    pieceMovementPhase.performMove(cellPane, currentPlayer);
                    if (currentPlayer.hasPieceToMove()) {
                        return;
                    }
                    break;
                case FLY_RULE:
                    //add fly rule Phase
                    FlyRulePhase flyRulePhase = (FlyRulePhase) phaseMap.get(phaseEnum.FLY_RULE);
                    flyRulePhase.performMove(cellPane, currentPlayer);
                    if (currentPlayer.hasPieceToMove()) {
                        return;
                    }
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
            getActivePlayer().setGamePhase(Player.Phase.GAME_OVER);
            setError(getCurrentPlayerColor() + " won!");
            return;
        }
        switchTurn();
    }



    public GameManager(TestFileDataGenerator testFileDataGenerator) {
        this.currentPlayer = defaultPlayer;
        this.testFileDataGenerator = testFileDataGenerator;
        setup();
    }

    private void setup() {
        player = new EnumMap<PlayerColor, Player>(PlayerColor.class);
        for (PlayerColor playerColor : PlayerColor.values()) {
            player.put(playerColor, new Player(playerColor));
        }
        phaseMap = new EnumMap<phaseEnum, IPhase>(phaseEnum.class);
        phaseMap.put(phaseEnum.PIECE_PLACEMENT, new PiecePlacementPhase(this));
        phaseMap.put(phaseEnum.PIECE_MOVEMENT, new PieceMovementPhase(this));
        //test fly
        phaseMap.put(phaseEnum.FLY_RULE, new FlyRulePhase(this));
    }

    public PlayerColor getCurrentPlayerColor() {
        return this.currentPlayer;
    }

    public Player getCurrentPlayer() {
        return player.get(this.currentPlayer);
    }

    public Player getInactivePlayer() {
        return player.get(this.currentPlayer.complement());
    }

    public Player getActivePlayer() {
        return player.get(this.currentPlayer);
    }

    public CellState getOpponentCellState() {return player.get(this.currentPlayer.complement()).getPlayerColorAsCellState();}

    public void resetMill(){
        this.millIsFormed = false;
    }

    public void setMillIsFormedToTrue() {
        this.millIsFormed = true;
        announcePhaseChange();
    }

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
                phaseListener.onPhaseChange(getCurrentPlayer().getGamePhase());
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
            int blackPieces = player.get(PlayerColor.BLACK).getPiecesInHand();
            int whitePieces = player.get(PlayerColor.WHITE).getPiecesInHand();
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
        turnChanged();
    }

    private void turnChanged() {
        if (turnChangeListener != null) {
            turnChangeListener.onTurnSwitch(getCurrentPlayerColor());
        }
    }

    public void switchTurn() {
        currentPlayer = currentPlayer.complement();
        turnChanged();
        announcePhaseChange();
    }


    public void resetGameManager() {
        currentPlayer = defaultPlayer;
        for (Player aPlayer: player.values()) {
            aPlayer.reset();
        }
        announcePiecesInHandChange();
        millIsFormed = false;
        isGameOver = false;
        setError("");
        turnChanged();
        announcePhaseChange();
        allPlacedPieces.clear();
        if (testFileDataGenerator != null) {
            testFileDataGenerator.reset();
        }
    }

    public void addPlacedPieceMoves(CellPane cell){
        if (cell.up != null && cell.up.cellState == CellState.EMPTY){
            getActivePlayer().validMovesCounter++;
        }
        if (cell.down != null && cell.down.cellState == CellState.EMPTY){
            getActivePlayer().validMovesCounter++;
        }
        if (cell.left != null && cell.left.cellState == CellState.EMPTY){
            getActivePlayer().validMovesCounter++;
        }
        if (cell.right != null && cell.right.cellState == CellState.EMPTY){
            getActivePlayer().validMovesCounter++;
        }

    }

    public void removePieceMoves(CellPane cell){
        Player playersPieces = getActivePlayer();
        if(cell.cellState == getInactivePlayer().getPlayerColorAsCellState()) {
            playersPieces = getInactivePlayer();
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
        if (cell.up!= null && cell.up.cellState == getActivePlayer().getPlayerColorAsCellState()){
            getActivePlayer().validMovesCounter++;
        }
        if (cell.up != null && cell.up.cellState == getInactivePlayer().getPlayerColorAsCellState()){
            getInactivePlayer().validMovesCounter++;
        }
        if (cell.down != null && cell.down.cellState == getActivePlayer().getPlayerColorAsCellState()){
            getActivePlayer().validMovesCounter++;
        }
        if (cell.down != null && cell.down.cellState == getInactivePlayer().getPlayerColorAsCellState()){
            getInactivePlayer().validMovesCounter++;
        }
        if (cell.left != null && cell.left.cellState == getActivePlayer().getPlayerColorAsCellState()){
            getActivePlayer().validMovesCounter++;
        }
        if (cell.left != null && cell.left.cellState == getInactivePlayer().getPlayerColorAsCellState()){
            getInactivePlayer().validMovesCounter++;
        }
        if (cell.right != null && cell.right.cellState == getActivePlayer().getPlayerColorAsCellState()){
            getActivePlayer().validMovesCounter++;
        }
        if (cell.right != null && cell.right.cellState == getInactivePlayer().getPlayerColorAsCellState()){
            getInactivePlayer().validMovesCounter++;
        }
    }

    //removes moves for surrounding pieces when a piece is PLACED
    public void removeMoves(CellPane cell){
        if (cell.up != null && cell.up.cellState == getActivePlayer().getPlayerColorAsCellState()){
            getActivePlayer().validMovesCounter--;
        }
        if (cell.up != null && cell.up.cellState == getInactivePlayer().getPlayerColorAsCellState()){
            getInactivePlayer().validMovesCounter--;
        }
        if (cell.down != null && cell.down.cellState == getActivePlayer().getPlayerColorAsCellState()){
            getActivePlayer().validMovesCounter--;
        }
        if (cell.down != null && cell.down.cellState == getInactivePlayer().getPlayerColorAsCellState()){
            getInactivePlayer().validMovesCounter--;
        }
        if (cell.left != null && cell.left.cellState == getActivePlayer().getPlayerColorAsCellState()){
            getActivePlayer().validMovesCounter--;
        }
        if (cell.left != null && cell.left.cellState == getInactivePlayer().getPlayerColorAsCellState()){
            getInactivePlayer().validMovesCounter--;
        }
        if (cell.right != null && cell.right.cellState == getActivePlayer().getPlayerColorAsCellState()){
            getActivePlayer().validMovesCounter--;
        }
        if (cell.right != null && cell.right.cellState == getInactivePlayer().getPlayerColorAsCellState()){
            getInactivePlayer().validMovesCounter--;
        }
    }
}
