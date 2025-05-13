package unogame;

public class NumberCard extends Card {
    public NumberCard(Color color, Value value) {
        super(color, value);
        if (value.ordinal() > Value.NINE.ordinal() || value.ordinal() < Value.ZERO.ordinal()) {
            throw new IllegalArgumentException("NumberCard must have a numeric value (0-9), not " + value);
        }
        if (color == Color.WILD) {
            throw new IllegalArgumentException("NumberCard cannot have WILD color");
        }
    }

    @Override
    public void applyEffect(GameController gameController) {
        // No special effect
    }
}

