package projectone;

public class NumberCard extends Card {
    public NumberCard(Color color, Value value) {
        super(color, value);
        if (value.ordinal() > Value.NINE.ordinal() || value.ordinal() < Value.ZERO.ordinal()) {
            throw new IllegalArgumentException("COMP2033_Project_1_110450836.NumberCard must have a numeric value (0-9), not " + value);
        }
        if (color == Color.WILD) {
            throw new IllegalArgumentException("COMP2033_Project_1_110450836.NumberCard cannot have WILD color");
        }
    }

    @Override
    public void applyEffect(UnoGame gameController) {
        // No special effect
    }
}
