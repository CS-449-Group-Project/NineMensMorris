package Morris_FX.Logic;

import Morris_FX.Ui.CellPane;

public interface PhaseInterface {

    void performMove(CellPane cellPane);
    boolean validateCellSelection(CellPane cell);
    @Override
    String toString();
}
