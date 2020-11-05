package test;

import Morris_FX.Logic.CellPosition;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class CellPositionTest {
    @Test
    public void testCenterPositionRotation() {
        CellPosition initialPos = new CellPosition(3,0);
        HashMap<Integer, CellPosition> anglePosMap = new HashMap<>();
        anglePosMap.put(0, new CellPosition(3,0));
        anglePosMap.put(90, new CellPosition(0,3));
        anglePosMap.put(180, new CellPosition(3,6));
        anglePosMap.put(270, new CellPosition(6,3));

        for (Map.Entry anglePos : anglePosMap.entrySet()) {
            int angle = (int)anglePos.getKey();
            CellPosition oracle = (CellPosition)anglePos.getValue();
            CellPosition result = initialPos.rotateCounterClockwise(Math.toRadians(angle));
            assertTrue(result.getRow() == oracle.getRow());
            assertTrue(result.getColumn() == oracle.getColumn());
        }
    }

    @Test
    public void testCornerPositionRotation() {
        CellPosition initialPos = new CellPosition(0,0);
        HashMap<Integer, CellPosition> anglePosMap = new HashMap<>();
        anglePosMap.put(0, new CellPosition(0,0));
        anglePosMap.put(90, new CellPosition(0,6));
        anglePosMap.put(180, new CellPosition(6,6));
        anglePosMap.put(270, new CellPosition(6,0));

        for (Map.Entry anglePos : anglePosMap.entrySet()) {
            int angle = (int)anglePos.getKey();
            CellPosition oracle = (CellPosition)anglePos.getValue();
            CellPosition result = initialPos.rotateCounterClockwise(Math.toRadians(angle));
            assertTrue(result.getRow() == oracle.getRow());
            assertTrue(result.getColumn() == oracle.getColumn());
        }
    }


    @Test
    public void testAngleTo() {
        CellPosition[] initialPositions = {
                new CellPosition(0,0),
                new CellPosition(3,0)
        };
        int[] testAngles = {90,180,270,360};
        for (CellPosition initialPos: initialPositions) {
            for (int testAngle: testAngles) {
                int targetDegrees = 360 - testAngle;
                CellPosition targetPos = initialPos.rotateCounterClockwise(Math.toRadians(testAngle));
                assertTrue(targetPos.angleTo(initialPos) == targetDegrees);
            }
        }
    }

}
