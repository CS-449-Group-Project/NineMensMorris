package Utils;

import Morris_FX.Logic.CellPosition;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class TestCaseGenerator implements  Iterable<CellPosition> {

    private Vector<CellPosition> moves = new Vector<>(10);
    private Map<Short, CellPosition> movesCache = new HashMap<>(10);
    private int gridSize;


    public static TestCaseGenerator createFromFile(String filePath) throws IOException {
        TestCaseGenerator testCaseGenerator = new TestCaseGenerator();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(filePath));
        int headerLength = 8;
        byte[] header = new byte[headerLength];
        reader.read(header, 0, headerLength);

        int magicNumber = readTwoBytesAsShort(header,0);
        if (magicNumber != TestFileDataGenerator.magicNumber) {
            throw new Error("Invalid magic number");
        }
        // int version = header[1] << ; // ignore version number for now

        int gridSizeOffset = 2 * 2;
        int gridSize = readTwoBytesAsShort(header, gridSizeOffset);
        testCaseGenerator.setGridSize(gridSize);

        int positionCountOffset = 3 * 2;
        short positionsAmount = readTwoBytesAsShort(header, positionCountOffset);
        byte[] compressedPositions = new byte[positionsAmount * 2];
        reader.read(compressedPositions);
        testCaseGenerator.setupPositionsFromByteArray(compressedPositions);
        return testCaseGenerator;
    }

    private void setupPositionsFromByteArray(byte[] compressedPositions) {
        for (int i = 0; i < compressedPositions.length; i += 2) {
            short compressedPosition = readTwoBytesAsShort(compressedPositions, i);
            if (!movesCache.containsKey(compressedPosition)) {
                movesCache.put(compressedPosition, decompressPosition(compressedPosition));
            }
            CellPosition cellPos = movesCache.get(compressedPosition);
            moves.add(cellPos);
        }
    }

    private static short readTwoBytesAsShort(byte[] buffer, int offset) {
        short value = (short)(buffer[offset] << 8 | buffer[offset + 1]);
        return value;
    }

    public int getExpectedGridSize() {
        return gridSize;
    }

    private void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    private CellPosition decompressPosition(short compressedPosition) {
        int column = compressedPosition >> 8;
        int row = compressedPosition & 255;
        return new CellPosition(column, row);
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
