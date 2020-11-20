package Utils;

import Morris_FX.Logic.CellPosition;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class TestFileDataGenerator {
    public static short magicNumber = 0x5444; // TD
    private int gridSize = 0;
    private short version = 0;
    private Vector<CellPosition> positions = new Vector<>(10);

    public TestFileDataGenerator(int gridSize) {
        this.gridSize = gridSize;
    }

    public void addPosition(CellPosition position) {
        positions.add(position);
    }

    public boolean generateFile(String filePath) {
        try {
            FileOutputStream fileWriter = new FileOutputStream(filePath);
            fileWriter.write(convertDataToByteArray());
            fileWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
        return true;
    }

    private byte[] convertDataToByteArray() {
        int headerSize = 4;
        int size = positions.size() + headerSize;

        byte[] buffer = new byte[size * 2];

        short[] header = {
                magicNumber,
                version,
                (short)gridSize,
                (short)positions.size()
        };

        int offset = 0;
        for (short headerProperty : header) {
            offset = writeShortToBuffer(buffer, offset, headerProperty);
        }

        for (int i = 0; i < positions.size(); i++) {
            CellPosition pos = positions.get(i);
            short compressedPos = compressPosition(pos);
            offset = writeShortToBuffer(buffer, offset, compressedPos);
        }
        return buffer;
    }

    private int writeShortToBuffer(byte[] buffer, int offset, short value) {
        buffer[offset] = (byte)(value >> 8);
        buffer[offset + 1] = (byte)(value & 0xFF);
        return offset + 2;
    }


    private short compressPosition(CellPosition pos) {
        // column | row
        short compressedPos = (short)((pos.getColumn() << 8) | pos.getRow());
        return compressedPos;
    }

    public void reset() {
        positions.clear();
    }
}
