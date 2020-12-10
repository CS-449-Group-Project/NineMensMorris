package Morris_FX.Ui;

import Morris_FX.Logic.Player;
import Morris_FX.Logic.PlayerColor;
import Morris_FX.Logic.TurnContext;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TurnTextField extends TextField implements PropertyChangeListener {

    public TurnTextField() {
        super();
        setMaxWidth(120);
        setDisable(true);
        setStyle("-fx-opacity: 1;");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        System.out.println(propertyName);
        if ("player".equals(propertyName)) {
            Player player = (Player)evt.getNewValue();
            setText(player.getColor() + "'s turn.");
        }
    }
}
