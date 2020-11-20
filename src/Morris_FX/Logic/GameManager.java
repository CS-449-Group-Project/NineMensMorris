package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;
import Utils.TestFileDataGenerator;

import java.sql.SQLOutput;
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

    // for PIECE_PLACEMENT phase this method sets the state of the clicked cell equal to the player color; "Places current
    // player piece on the board"
    // for PIECE_MOVEMENT phase this method sets the pieceToMove variable of the current player to the clicked cell if they
    // dont have one to move already; "Selects a piece"
    // if they do have a piece to move already then it sets the state of the clicked cell to the player color, sets the
    // previously occupied cell to empty, and set pieceToMove to null in the current player object; "Places pieceToMove in new position"
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

                    currentPlayer.removePiecesFromHand();
                    announceMarblesInHandChange();
                    cellPane.setState(currentPlayer.getPlayerColorAsCellState());
                    getCurrentPlayer().increaseBoardPieces();
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
            getActivePlayer().setGamePhase(Player.Phase.GAME_OVER);
            setError(getCurrentPlayerColor() + " won!");
            return;
        }
        switchTurn();
    }

    public GameManager() {
        this.currentPlayer = defaultPlayer;
        setup();
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


    public interface MarblesInHandListener {
        void marblesInHandChange(int blackMarbles, int whiteMarbles);
    }

    private MarblesInHandListener marblesInHandListener = null;
    public void onMarblesInHandChange(MarblesInHandListener listener) {
        marblesInHandListener = listener;
        announceMarblesInHandChange();
    }

    public void announceMarblesInHandChange() {
        if (marblesInHandListener != null) {
            int blackMarbles = player.get(PlayerColor.BLACK).getPiecesInHand();
            int whiteMarbles = player.get(PlayerColor.WHITE).getPiecesInHand();
            marblesInHandListener.marblesInHandChange(blackMarbles, whiteMarbles);
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
        announceMarblesInHandChange();
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
        Player playersMarble = getActivePlayer();
        if(cell.cellState == getInactivePlayer().getPlayerColorAsCellState()) {
            playersMarble = getInactivePlayer();
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

    //removes moves for surrounding marbles when a piece is PLACED
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
