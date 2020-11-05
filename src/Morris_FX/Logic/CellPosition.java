package Morris_FX.Logic;

public class CellPosition {
    public static CellPosition origin = new CellPosition(3,3);

    private final int row, column;

    public CellPosition(int column, int row) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    public String toString() {
        return String.format("(%d,%d)", column, row);
    }

    // assume degrees is counterclockwise 0,90,180,270
    public CellPosition rotateCounterClockwise(double rads) {
        CellPosition normalized = normalizeWithOrigin();
        int normY = normalized.getRow();
        int normX = normalized.getColumn();
        long newCol = Math.round(normX * Math.cos(rads) + normY * Math.sin(rads));
        long newRow = Math.round(-normX * Math.sin(rads) + normY * Math.cos(rads));
        return new CellPosition(Math.toIntExact(newCol) + origin.getColumn(), Math.abs(Math.toIntExact(newRow) + origin.getRow()));
    }

    public int angleTo(CellPosition target) {
        int degrees = Math.toIntExact(Math.round((toAngle() - target.toAngle()) * 180/Math.PI));
        return degrees > 0 ? 360 - degrees : Math.abs(degrees);
    }

    public double toAngle() {
        CellPosition normalized = normalizeWithOrigin();
        int x = normalized.getColumn(), y = normalized.getRow();
        if (x == 0) {
            return y < 0 ? Math.PI/2 : (3 * Math.PI)/2;
        } else if (y == 0) {
            return x > 0 ? 0 : Math.PI;
        }

        double angle = Math.atan(-y/x);
        if (x > 0 && y > 0) {
            return angle;
        }
        if (x > 0 && y <= 0) {
            return angle + 2 * Math.PI;
        }

        return angle + Math.PI;
    }


    private CellPosition normalizeWithOrigin() {
        return new CellPosition(column - origin.getColumn(), row - origin.getRow());
    }
}