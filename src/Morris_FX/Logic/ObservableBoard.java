package Morris_FX.Logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableBoard extends Board {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ObservableBoard(GameState state) {
        super(state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    protected void setCell(CellPosition position, CellState newState) {
        Cell cell = getCell(position);
        pcs.fireIndexedPropertyChange("grid", position.toIndex(), cell.getState(), newState);
        cell.setState(newState);
    }

}
