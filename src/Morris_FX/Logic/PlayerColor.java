package Morris_FX.Logic;

public enum PlayerColor {
    BLACK,
    WHITE;

    public PlayerColor complement() {
        return this == PlayerColor.BLACK ?
                        PlayerColor.WHITE :
                        PlayerColor.BLACK;
    }
    public String toString() {
        return this == PlayerColor.BLACK ? "Black" : "White";
    }
}
