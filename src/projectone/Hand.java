package projectone; /**
 * File: projectone.Hand.java
 * Description:  Represents a player's hand of Uno cards. Uses an ArrayList and explicitly sorts cards after each addition to maintain sorted order as per the requirement.
 * Author: Andrew Nell 
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tool Used: Copilot for boilerplate code and some comments
 * This is my own work as defined by he University's Academic Integrity Policy.
 **/
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Hand {
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    /**
     * Adds a single card to the hand and re-sorts the hand.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot add null card to hand");
        }
        cards.add(card);
        sortHand(); // Maintain sorted order
    }

    /**
     * Adds multiple cards to the hand and re-sorts the hand.
     * @param cardsToAdd The list of cards to add.
     */
    public void addCards(List<Card> cardsToAdd) {
        if (cardsToAdd == null || cardsToAdd.contains(null)) {
             throw new IllegalArgumentException("Cannot add null list or list with null cards");
        }
        this.cards.addAll(cardsToAdd);
        sortHand(); // Maintain sorted order
    }

    /**
     * Attempts to remove a specific card instance from the hand.
     * Relies on projectone.Card.equals().
     * @param card The card instance to remove.
     * @return true if the card was found and removed, false otherwise.
     */
    public boolean removeCard(Card card) {
        return cards.remove(card); // Sort order is maintained by removal
    }

    /**
     * Finds and removes the specified card, returning it.
     * Useful for playing a card. Maintains sorted order.
     * @param cardToPlay The card to find and remove.
     * @return The card that was played.
     * @throws IllegalArgumentException if the card is not in the hand.
     */
     public Card playCard(Card cardToPlay) {
         boolean removed = cards.remove(cardToPlay);
         if (!removed) {
             throw new IllegalArgumentException("projectone.Card " + cardToPlay + " not found in hand.");
         }
         // Sort order is maintained by removal, no need to re-sort
         return cardToPlay;
     }

     /**
      * Finds and removes a card at a specific index.
      * Used if selection is by index rather than card object.
      * @param index The index of the card to remove.
      * @return The card that was removed.
      * @throws IndexOutOfBoundsException if the index is invalid.
      */
     public Card playCard(int index) {
         return cards.remove(index); // Sort order maintained
     }


    /**
     * Sorts the hand according to the projectone.Card's natural order (Comparable).
     * Called automatically after cards are added.
     */
    private void sortHand() {
        Collections.sort(cards);
    }

    /**
     * Finds all cards in the hand that can be legally played on the current pile top card.
     * @param topPileCard The card currently on top of the pile.
     * @param chosenWildColor The active chosen wild color, if applicable.
     * @return A List of playable cards (may be empty).
     */
    public List<Card> findValidCards(Card topPileCard, Color chosenWildColor) {
        List<Card> validPlays = new ArrayList<>();
        for (Card card : cards) {
            if (card.canPlayOn(topPileCard, chosenWildColor)) {
                validPlays.add(card);
            }
        }
        return validPlays;
    }

    /**
     * Returns an unmodifiable view of the cards in the hand.
     * Prevents external modification of the hand's internal list.
     * @return An unmodifiable list of cards.
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int getSize() {
        return cards.size();
    }

    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "projectone.Hand: []";
        }
        // Joining card strings with ", " - leverages sorted order
        return "projectone.Hand: [" + cards.stream()
                                .map(Card::toString)
                                .collect(Collectors.joining(", ")) + "]";
    }

     // equals() and hashCode() are less critical for projectone.Hand, focus on projectone.Card's.
}
