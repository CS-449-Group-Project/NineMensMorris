package Utils;

import Morris_FX.Logic.CellPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TestGenerator {
    public static StringBuilder generateFromCellPositions(Vector<CellPosition> positions, Vector<String> piecePlacementComments) {
        StringBuilder testMethod = new StringBuilder();

        // instantiate only what is needed
        Map<String, String> map = new HashMap<String, String>();

        for (int i = 0; i < positions.size(); i++) {
            CellPosition pos = positions.get(i);
            String varName = pos.getHumanString();
            String lineComment = piecePlacementComments.get(i);
            testMethod.append(String.format("%s %s\n", varName, lineComment));
        }
        return testMethod;
    }

    private static boolean inVector(Vector<CellPosition> vector, CellPosition other) {
        return vector.stream().filter(adjacentPosition -> {
            return adjacentPosition.matches(other);
        }).count() == 1;
    }

}
