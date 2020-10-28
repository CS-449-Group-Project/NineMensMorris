package Morris_FX.Logic;

public enum PlayerColor {
    BLACK,
    WHITE;

    public PlayerColor complement() {
        return this == PlayerColor.BLACK ?
                        PlayerColor.WHITE :
                        PlayerColor.BLACK;
    }
}
