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

/**
 * Represents a single Uno card with a color and value.
 * Implements Comparable for sorting purposes (primarily for Hand).
 */
public class Card implements Comparable<Card> {
    public enum Color {
        RED, YELLOW, GREEN, BLUE, WILD
    }
    public enum Value {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, // Number cards
        SKIP, REVERSE, DRAW_TWO,                                  // Action cards
        WILD, WILD_DRAW_FOUR                                      // Wild cards
    }

    private final Color color;
    private final Value value;

    /**
     * Constructs a new Card.
     * @param color The color of the card.
     * @param value The value or action of the card.
     */
    public Card(Color color, Value value) {
        if (color == null || value == null) {
            throw new IllegalArgumentException("Card color and value cannot be null");
        }
        // Basic validation: Wild cards must have WILD color
        if ((value == Value.WILD || value == Value.WILD_DRAW_FOUR) && color != Color.WILD) {
            throw new IllegalArgumentException("Wild cards must have WILD color");
        }
        // Basic validation: Non-wild cards cannot have WILD color
        if (color == Color.WILD && !(value == Value.WILD || value == Value.WILD_DRAW_FOUR)) {
             throw new IllegalArgumentException("Only Wild cards can have WILD color");
        }

        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

    /**
     * Checks if this card can be legally played on the given top card of the pile.
     * Takes into account color matching, value matching, and wild card rules.
     * Does NOT account for the chosen color of a played WILD card - that's game logic.
     *
     * @param topPileCard The card currently on top of the discard pile.
     * @param chosenWildColor The color chosen if the topPileCard is a WILD, otherwise null.
     * @return true if this card can be played, false otherwise.
     */
    public boolean canPlayOn(Card topPileCard, Color chosenWildColor) {
        if (topPileCard == null) { // Can play anything on an empty pile (shouldn't happen in standard Uno start)
           return true;
        }

        // Wild cards can be played on anything
        if (this.color == Color.WILD) {
            return true;
        }

        // Match color (considering chosen wild color)
        Color effectiveTopColor = (topPileCard.getColor() == Color.WILD && chosenWildColor != null)
                                  ? chosenWildColor : topPileCard.getColor();
        if (this.color == effectiveTopColor) {
            return true;
        }

        // Match value (non-wild cards only)
        if (this.value == topPileCard.getValue()) {
            return true;
        }

        return false;
    }


    /**
     * Compares this card to another card for sorting purposes.
     * Sorts primarily by Color (RED, YELLOW, GREEN, BLUE, WILD)
     * then by Value (Numbers, Actions, Wilds).
     */
    @Override
    public int compareTo(Card other) {
        // Compare by color first
        int colorCompare = this.color.compareTo(other.color);
        if (colorCompare != 0) {
            return colorCompare;
        }
        // If colors are the same, compare by value
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return color + " " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return color == card.color && value == card.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, value);
    }
}