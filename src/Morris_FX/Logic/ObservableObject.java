package Morris_FX.Logic;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableObject {

    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

}
