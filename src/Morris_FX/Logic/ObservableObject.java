package Morris_FX.Logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableObject {

    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    protected <T> void firePropertyChange(String propertyName, T newValue) {
        for(PropertyChangeListener listener: propertyChangeSupport.getPropertyChangeListeners()) {
            listener.propertyChange(new PropertyChangeEvent(this, propertyName, newValue, newValue));
        }
    }
}
