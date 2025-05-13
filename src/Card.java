/**
 * File: Card.java
 * Description: A brief description of this Java module.
 * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tool Used: Copilot for boilerplate code and some comments
 * This is my own work as defined by the University's Academic Integrity Policy.
 **/

import java.util.Objects;

public abstract class Card implements Comparable<Card> {
    protected final Color color;
    protected final Value value;

    protected Card(Color color, Value value) {
        Objects.requireNonNull(color, "Card color cannot be null");
        Objects.requireNonNull(value, "Card value cannot be null");
        validateColorValueCombination(color, value);
        this.color = color;
        this.value = value;
    }

    private void validateColorValueCombination(Color color, Value value) {
        boolean isWildValueType = (value == Value.WILD || value == Value.WILD_DRAW_FOUR);
        if (isWildValueType && color != Color.WILD) {
            throw new IllegalArgumentException("Cards with value " + value + " must have WILD color, not " + color);
        }
        if (color == Color.WILD && !isWildValueType) {
            throw new IllegalArgumentException("Only WILD or WILD_DRAW_FOUR cards can have WILD color. Value was: " + value);
        }
    }

    public Color getColor() { return color; }
    public Value getValue() { return value; }

    public abstract void applyEffect(Game gameController);

    public boolean canPlayOn(Card topPileCard, Color activeWildColorOnPile) {
        if (topPileCard == null) return true; 
        if (this.color == Color.WILD) return true;

        Color effectiveTopColor = topPileCard.getEffectiveColor(activeWildColorOnPile);
        if (this.color == effectiveTopColor) return true; 

        if (topPileCard.getColor() != Color.WILD && this.value == topPileCard.getValue()) {
            return true;
        }
        return false;
    }
    
    public Color getEffectiveColor(Color chosenColorForThisWildCard) {
        return (this.color == Color.WILD && chosenColorForThisWildCard != null) ? chosenColorForThisWildCard : this.color;
    }

    @Override
    public int compareTo(Card other) {
        Objects.requireNonNull(other, "Cannot compare to a null card.");
        int colorCompare = this.color.compareTo(other.color);
        if (colorCompare != 0) return colorCompare;
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() { return color + " " + value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return color == card.color && value == card.value;
    }

    @Override
    public int hashCode() { return Objects.hash(color, value); }
}
