package Utils;

import Morris_FX.Logic.CellPosition;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.function.Consumer;

public class TestCaseGenerator implements  Iterable<CellPosition> {

    private Vector<CellPosition> moves = new Vector<>(10);
    private Map<String, CellPosition> movesCache = new HashMap<>(10);
    private String filePath;
    public TestCaseGenerator(String filePath) throws IOException {
        this.filePath = filePath;
        setup();
    }

    private void setup() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }
            String humanName = line.substring(0, 2);
            if (!movesCache.containsKey(humanName)) {
                movesCache.put(humanName, CellPosition.createFromHumanString(humanName));
            }

            CellPosition cellPos = movesCache.get(humanName);
            moves.add(cellPos);
        }
    }


    @Override
    public Iterator<CellPosition> iterator() {
        Iterator<CellPosition> movesIterator = new Iterator<CellPosition>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < moves.size();
            }

            @Override
            public CellPosition next() {
                return moves.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return movesIterator;
    }

}
