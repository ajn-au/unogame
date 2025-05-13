
/**
 * Represents an action card in an Uno game (Skip, Reverse, Draw Two).
 *
 * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tools Used: None
 * This is my own work as defined by the University's Academic Integrity Policy.
 */

public class ActionCard extends Card {
    public ActionCard(Color color, Value value) {
        super(color, value);
        if (!(value == Value.SKIP || value == Value.REVERSE || value == Value.DRAW_TWO)) {
            throw new IllegalArgumentException("ActionCard must be SKIP, REVERSE, or DRAW_TWO, not " + value);
        }
        if (color == Color.WILD) {
            throw new IllegalArgumentException("ActionCard cannot have WILD color");
        }
    }

    @Override
    public void applyEffect(EffectHandler gameController) {
        // The EffectHandler now manages all card effects.
        // This method is intentionally left empty.
    }
}
