package projectone; /**
 * File: COMP2033_Project_1_110450836.Pile.java
 * Description: Represents the discard pile in an Uno game.
 * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tool Used: 
 * This is my own work as defined by the University's Academic Integrity Policy.
 **/
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
/**
 * Represents a pile of cards, specifically the discard pile in an Uno game.
 * This class allows adding cards to the pile, peeking at the top card,
 * and reshuffling cards for the deck.
 */
public class Pile {
    // Use Deque for efficient addFirst (push) and peekFirst (peek)
    private final Deque<Card> cards;

    public Pile() {
        cards = new ArrayDeque<>();
    }

    /**
     * Adds a card to the top of the pile.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot add null card to pile");
        }
        cards.addFirst(card); // Add to the "top"
    }

    /**
     * Returns the card currently on top of the pile without removing it.
     * @return The top card, or null if the pile is empty.
     */
    public Card getTopCard() {
        return cards.peekFirst(); // Peek at the "top"
    }

    /**
     * Takes all cards except the top card from the pile, leaving the pile
     * with only the current top card. Used for reshuffling into the deck.
     *
     * @return A list of cards that were below the top card. Returns an empty list
     *         if the pile had 0 or 1 cards.
     */
    public List<Card> takeCardsForNewDeck() {
        if (cards.size() <= 1) {
            return new ArrayList<>(); // Nothing to reshuffle
        }

        Card topCard = cards.removeFirst(); // Temporarily remove top
        List<Card> cardsToReshuffle = new ArrayList<>(cards); // Copy the rest
        cards.clear(); // Clear the deque
        cards.addFirst(topCard); // Put the original top card back

        return cardsToReshuffle;
    }

     /**
      * Checks if a card can be legally played on the current top card.
      * Considers the chosen wild color if the top card is WILD.
      * @param card The card to check.
      * @param chosenWildColor The active chosen color if the top card is wild.
      * @return true if the card can be played, false otherwise.
      */
     public boolean canPlay(Card card, Color chosenWildColor) {
         return card.canPlayOn(getTopCard(), chosenWildColor);
     }

    @Override
    public String toString() {
        Card top = getTopCard();
        return "COMP2033_Project_1_110450836.Pile [Top COMP2033_Project_1_110450836.Card: " + (top == null ? "None" : top.toString()) + "]";
    }
}
