package test;

import Morris_FX.Logic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTests {
    private Turn turn;
    private GameState gameState;
    private Board board;


    @BeforeEach
    public void setup() {
        turn = new Turn(PlayerColor.WHITE);
        gameState = new GameState(turn);
        board = new Board(gameState);
    }

    @Test
    public void test() {
        CellPosition p1 = new CellPosition(0,3);
        CellPosition p2 = new CellPosition(3,0);

        List<CellPosition> validMoves = board.getValidMoves(p1);
        List<CellPosition> validMoves2 = board.getValidMoves(p2);
        for (int i = 0; i < validMoves.size(); i++) {

            CellPosition oraclePos1 = validMoves.get(i);
            double radians = Math.toRadians(-90);
            // Not testing CellPosition so this should be true
            CellPosition rotatedPos1 = oraclePos1.rotateCounterClockwise(radians);

            CellPosition oraclePos2 = validMoves2.get(i);
            assertTrue(rotatedPos1.getRow() == oraclePos2.getRow());
            assertTrue(rotatedPos1.getColumn() == oraclePos2.getColumn());
        }
    }

    @Test
    public void testValidMovesWithNonEmptyBoard() {
        CellPosition firstMove =  new CellPosition(0,0);
        CellPosition secondMove = new CellPosition(0,3);

        board.performMove(firstMove);
        board.performMove(secondMove);
        List<CellPosition> validMoves = board.getValidMoves(firstMove);
        System.out.println(validMoves);
        assertEquals(1, validMoves.size());

        CellPosition onlyValidMove = secondMove.rotateCounterClockwise(Math.toRadians(270));

        CellPosition firstChoice = validMoves.get(0);


        assertTrue(onlyValidMove.getColumn() == firstChoice.getColumn());
        assertTrue(onlyValidMove.getRow() == firstChoice.getRow());
    }
}
