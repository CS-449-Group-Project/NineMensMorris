package Morris_FX.Ui;

import Morris_FX.Logic.Player;
import Morris_FX.Logic.PlayerColor;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PiecesInHandTextField extends TextField implements PropertyChangeListener {
    private PlayerColor colorFilter;
    public PiecesInHandTextField(PlayerColor color) {
        colorFilter = color;
        setMinWidth(275/2);
        setMaxWidth(275/2);
        setDisable(true);
        setStyle("-fx-opacity: 1;");
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Player source = (Player) evt.getSource();
        if (source.getColor() == colorFilter) {
            String propertyName = evt.getPropertyName();
            if ("piecesInHand".equals(propertyName)) {
                int piecesInHand = (int)evt.getNewValue();
                setText(colorFilter + " marbles: " + piecesInHand);
            }
        }
    }
}
