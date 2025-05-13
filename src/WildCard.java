package unogame;

public class WildCard extends Card {
    public WildCard(Value value) {
        super(Color.WILD, value);
        if (!(value == Value.WILD || value == Value.WILD_DRAW_FOUR)) {
            throw new IllegalArgumentException("WildCard must be WILD or WILD_DRAW_FOUR, not " + value);
        }
    }

    @Override
    public void applyEffect(GameController gameController) {        
        // The EffectHandler now manages all card effects.
        // This method is intentionally left empty.
    }
}
}
