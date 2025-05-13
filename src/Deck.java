/**
 * File: Deck.java
 * Description: A brief description of this Java module.
 * Author: Steve Jobs
 * Student ID: 12345678
 * Email ID: jobst007
 * AI Tool Used: Y/N (This includes all AI Tools e.g. ChatGPT, Microsoft or Github Copiliot etc... Please leave blank if you do not wish to share this information)
 * This is my own work as defined by
 *    the University's Academic Integrity Policy.
 **/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random; // Needed for Collections.shuffle with seed if desired

public class Deck {
    private final List<Card> cards;

    /**
     * Creates a new standard Uno deck with 108 cards and shuffles it.
     */
    public Deck() {
        cards = new ArrayList<>(108);
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
         // Standard colors (non-WILD)
        Color[] standardColors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};

        for (Color color : standardColors) {
            // One Zero card
            cards.add(new Card(color, Value.ZERO));
            // Two of each number 1-9
            for (int i = 1; i <= 9; i++) {
                cards.add(new Card(color, Value.values()[i])); // Assumes enum order 0-9
                cards.add(new Card(color, Value.values()[i]));
            }
            // Two of each action card
            cards.add(new Card(color, Value.SKIP));
            cards.add(new Card(color, Value.SKIP));
            cards.add(new Card(color, Value.REVERSE));
            cards.add(new Card(color, Value.REVERSE));
            cards.add(new Card(color, Value.DRAW_TWO));
            cards.add(new Card(color, Value.DRAW_TWO));
        }

        // Four WILD cards
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(Color.WILD, Value.WILD));
        }

        // Four WILD DRAW FOUR cards
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(Color.WILD, Value.WILD_DRAW_FOUR));
        }
    }

    /**
     * Shuffles the deck randomly.
     */
    public void shuffle() {
        // Provide a seed for reproducible tests if needed: new Random(System.currentTimeMillis())
        Collections.shuffle(cards);
    }

    /**
     * Draws a single card from the top of the deck.
     * @return The card drawn.
     * @throws IllegalStateException if the deck is empty.
     */
    public Card drawCard() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck.");
        }
        // Remove from the "top" (end of the list for efficiency)
        return cards.remove(cards.size() - 1);
    }

     /**
     * Draws multiple cards from the top of the deck.
     * @param numCards The number of cards to draw.
     * @return A list of cards drawn.
     * @throws IllegalStateException if not enough cards are available.
     */
     public List<Card> drawCards(int numCards) {
         if (numCards < 0) throw new IllegalArgumentException("Cannot draw negative cards");
         if (cardsLeft() < numCards) {
              throw new IllegalStateException("Not enough cards in deck to draw " + numCards);
         }
         List<Card> drawn = new ArrayList<>(numCards);
         for (int i = 0; i < numCards; i++) {
              drawn.add(drawCard());
         }
         return drawn;
     }

     /**
      * Adds cards back to the deck (used for reshuffling the pile).
      * Does not shuffle automatically; call shuffle() afterwards.
      * @param cardsToAdd The list of cards to add.
      */
     public void addCards(List<Card> cardsToAdd) {
         cards.addAll(cardsToAdd);
     }


    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int cardsLeft() {
        return cards.size();
    }

    @Override
    public String toString() {
        return "Deck [" + cardsLeft() + " cards left]";
    }

    // equals() and hashCode() are generally not needed for a standard deck concept
}/**
 * Represents the draw deck in an Uno game.
 * Uses ArrayList for O(1) access to the top card and efficient resizing.
 *
 * DESIGN JUSTIFICATION:
 * ArrayList was chosen over LinkedList because:
 * 1. Drawing from the top/end of ArrayList is O(1)
 * 2. We never need to insert cards in the middle of the deck
 * 3. ArrayList provides better memory locality for shuffling operations
 *
 */
public class Deck {
    private final List<Card> cards;
    private final Random random; // Store for reproducible tests
    
    /**
     * Creates a new standard Uno deck with 108 cards and shuffles it
     * using the current time as a seed.
     */
    public Deck() {
        this(System.currentTimeMillis()); // Use current time as seed
    }
    
    /**
     * Creates a new Uno deck with a specific random seed.
     * Useful for testing as it produces reproducible shuffles.
     * 
     * @param seed The random seed to use for shuffling
     */
    public Deck(long seed) {
        cards = new ArrayList<>(108); // Pre-size to 108 cards for efficiency
        random = new Random(seed);
        initializeDeck();
        shuffle();
    }
    
    /**
     * Shuffles the deck using the Fisher-Yates algorithm.
     * Time complexity: O(n) where n is the number of cards
     */
    public void shuffle() {
        Collections.shuffle(cards, random);
    }
    
    // Other methods unchanged
}
