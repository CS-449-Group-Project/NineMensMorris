package Utils;

import Morris_FX.Logic.CellPosition;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class TestFileDataGenerator {
    private int gridSize = 0;
    private Vector<CellPosition> positions;
    private Vector<String> comments;
    public TestFileDataGenerator(int gridSize) {
        this.gridSize = gridSize;
        positions = new Vector<>(10);
        comments = new Vector<>(10);
    }

    public void addPosition(CellPosition position) {
        positions.add(position);
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void updateLastComment(String extraComment) {
        String lastComment =  comments.remove(comments.size() - 1);
        comments.add(lastComment + extraComment);
    }

    public boolean generateFile(String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(String.format("./%s",fileName));
            fileWriter.append(gridSize + "\n");
            for (int i = 0; i < positions.size(); i++) {
                CellPosition pos = positions.get(i);
                String varName = pos.getHumanString();
                String lineComment = comments.get(i);
                fileWriter.append(String.format("%s %s\n", varName, lineComment));
            }
            fileWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
        return true;
    }
    public void reset() {
        positions.clear();
        comments.clear();
    }
}
