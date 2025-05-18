package COMP2033_Project_1_110450836;
/**
 * File: COMP2033_Project_1_110450836.Card.java
 * Description: Uno card that has color and value
 * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tool Used: Copilot for boilerplate code and some comments
 * This is my own work as defined by the University's Academic Integrity Policy.
 **/

import java.util.*;

public abstract class Card implements Comparable<Card> {
    protected final Color color;
    protected final Value value;

    protected Card(Color color, Value value) {
        Objects.requireNonNull(color, "COMP2033_Project_1_110450836.Card color cannot be null");
        Objects.requireNonNull(value, "COMP2033_Project_1_110450836.Card value cannot be null");
        validateColorValueCombination(color, value);
        this.color = color;
        this.value = value;
    }

    public boolean isSpecial() { return this.value == Value.SKIP || this.value == Value.REVERSE
            || this.value == Value.DRAW_TWO || this.value == Value.WILD_DRAW_FOUR || this.value == Value.WILD; }

    private void validateColorValueCombination(Color color, Value value) {
        boolean isWildValueType = (value == Value.WILD || value == Value.WILD_DRAW_FOUR);
        if (isWildValueType && color != Color.WILD) {
            throw new IllegalArgumentException("Cards with value " + value + " must have WILD color, not " + color);
        }
        if (color == Color.WILD && !isWildValueType) {
            throw new IllegalArgumentException("Only WILD or WILD_DRAW_FOUR cards can have WILD color. COMP2033_Project_1_110450836.Value was: " + value);
        }
    }

    public Color getColor() { return color; }
    public Value getValue() { return value; }

    public abstract void applyEffect(UnoGame gameController);

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
        if (other == null) {
            return 1; // Place null cards at the end
        }
        List<Color> colorOrder = List.of(Color.WILD, Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
        int colorCompare = Integer.compare(
                colorOrder.indexOf(this.color),
                colorOrder.indexOf(other.color)
        );

        if (colorCompare != 0) {
            return colorCompare; // Prioritize sorting by color order
        }
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
