package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public class MillChecker {
    public static boolean millFormed(CellPane cell){
        return millFormed(cell, cell.cellState);
    }

    public static int countMillsFormed(CellPane cell, CellState targetState) {
        int millsFormed = 0;
        CellPane recursiveCell = cell;
        int vertical = 1;
        int horizontal = 1;

        //check vertical first, check up pointer until either the reference is null or the playstate is not the 'color' of the given cell
        while(recursiveCell.up != null && recursiveCell.up.cellState == targetState) {
            recursiveCell = recursiveCell.up;
            vertical++;
        }
        recursiveCell = cell;
        while(recursiveCell.down != null && recursiveCell.down.cellState == targetState) {
            recursiveCell = recursiveCell.down;
            vertical++;
        }
        if (vertical == 3) {
            millsFormed++;
        }
        recursiveCell = cell;

        //now check horizontal
        while(recursiveCell.left != null && recursiveCell.left.cellState == targetState) {
            recursiveCell = recursiveCell.left;
            horizontal++;
        }
        recursiveCell = cell;
        while(recursiveCell.right != null && recursiveCell.right.cellState == targetState) {
            recursiveCell = recursiveCell.right;
            horizontal++;
        }
        if (horizontal == 3) {
            millsFormed++;
        }
        return millsFormed;
    }

    public static boolean millFormed(CellPane cell, CellState targetState) {
        CellPane recursiveCell = cell;
        int vertical = 1;
        int horizontal = 1;

        //check vertical first, check up pointer until either the reference is null or the playstate is not the 'color' of the given cell
        while(recursiveCell.up != null && recursiveCell.up.cellState == targetState) {
            recursiveCell = recursiveCell.up;
            vertical++;
        }
        recursiveCell = cell;
        while(recursiveCell.down != null && recursiveCell.down.cellState == targetState) {
            recursiveCell = recursiveCell.down;
            vertical++;
        }
        if (vertical == 3) {
            return true;
        }
        recursiveCell = cell;

        //now check horizontal
        while(recursiveCell.left != null && recursiveCell.left.cellState == targetState) {
            recursiveCell = recursiveCell.left;
            horizontal++;
        }
        recursiveCell = cell;
        while(recursiveCell.right != null && recursiveCell.right.cellState == targetState) {
            recursiveCell = recursiveCell.right;
            horizontal++;
        }
        if (horizontal == 3) {
            return true;
        }
        return false;
    }

    public static boolean partOfPotentialMill(CellPane cell, CellState targetState) {
        // potential mills are always
        CellPane recursiveCell = cell;
        int vertical = 0;
        int horizontal = 0;

        boolean inMill = false;
        //check vertical first, check up pointer until either the reference is null or the playstate is not the 'color' of the given cell
        while(recursiveCell.up != null && recursiveCell.up.cellState == targetState) {
            recursiveCell = recursiveCell.up;
            vertical++;
        }

        recursiveCell = cell;
        while(recursiveCell.down != null && recursiveCell.down.cellState == targetState) {
            recursiveCell = recursiveCell.down;
            vertical++;
        }

        if (vertical == 1) {
            return true;
        }

        recursiveCell = cell;

        //now check horizontal
        while(recursiveCell.left != null && recursiveCell.left.cellState == targetState) {
            recursiveCell = recursiveCell.left;
            horizontal++;
        }
        recursiveCell = cell;
        while(recursiveCell.right != null && recursiveCell.right.cellState == targetState) {
            recursiveCell = recursiveCell.right;
            horizontal++;
        }

        return horizontal == 1;
    }

    public static boolean inPotentialMill(CellPane cell, CellPane suspect, CellState targetState) {
        CellPane recursiveCell = cell;
        int vertical = 1;
        int horizontal = 1;

        boolean inMill = false;
        //check vertical first, check up pointer until either the reference is null or the playstate is not the 'color' of the given cell
        while(recursiveCell.up != null && recursiveCell.up.cellState == targetState) {
            recursiveCell = recursiveCell.up;
            if (recursiveCell == suspect) inMill = true;
            vertical++;
        }

        recursiveCell = cell;
        while(recursiveCell.down != null && recursiveCell.down.cellState == targetState) {
            recursiveCell = recursiveCell.down;
            if (recursiveCell == suspect) inMill = true;
            vertical++;
        }
        if (inMill && vertical == 3) {
            return true;
        }

        inMill = false;
        recursiveCell = cell;

        //now check horizontal
        while(recursiveCell.left != null && recursiveCell.left.cellState == targetState) {
            recursiveCell = recursiveCell.left;
            if (recursiveCell == suspect) inMill = true;
            horizontal++;
        }
        recursiveCell = cell;
        while(recursiveCell.right != null && recursiveCell.right.cellState == targetState) {
            recursiveCell = recursiveCell.right;
            if (recursiveCell == suspect) inMill = true;
            horizontal++;
        }

        return horizontal == 3 && inMill;
    }
}
