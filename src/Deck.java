import java.util.*;

/**
 * Represents the Uno draw deck, containing all playable cards.
 * Supports draw, shuffle, and replenishment logic.
 *
 * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tools Used: 
 * This is my own work as defined by the University's Academic Integrity Policy.
 */
public class Deck {
    private final List<Card> cards;
    private final Random random;

    /**
     * Constructs a new shuffled deck using a random time-based seed.
     */
    public Deck() {
        this(System.currentTimeMillis());
    }

    /**
     * Constructs a new shuffled deck using a specific seed.
     * @param seed Random seed for reproducibility.
     */
    public Deck(long seed) {
        this.cards = new ArrayList<>(108);
        this.random = new Random(seed);
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        cards.clear();
        Color[] standardColors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};

        for (Color color : standardColors) {
            cards.add(new NumberCard(color, Value.ZERO));
            for (int i = 1; i <= 9; i++) {
                Value val = Value.values()[i];
                cards.add(new NumberCard(color, val));
                cards.add(new NumberCard(color, val));
            }
            cards.add(new ActionCard(color, Value.SKIP));
            cards.add(new ActionCard(color, Value.SKIP));
            cards.add(new ActionCard(color, Value.REVERSE));
            cards.add(new ActionCard(color, Value.REVERSE));
            cards.add(new ActionCard(color, Value.DRAW_TWO));
            cards.add(new ActionCard(color, Value.DRAW_TWO));
        }

        for (int i = 0; i < 4; i++) {
            cards.add(new WildCard(Color.WILD, Value.WILD));
            cards.add(new WildCard(Color.WILD, Value.WILD_DRAW_FOUR));
        }
    }

    /**
     * Shuffles the deck using the internal random generator.
     */
    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    /**
     * Draws a single card from the top of the deck.
     * @return The drawn card.
     * @throws IllegalStateException if the deck is empty.
     */
    public Card drawCard() {
        if (isEmpty()) throw new IllegalStateException("Cannot draw from an empty deck.");
        return cards.remove(cards.size() - 1);
    }

    /**
     * Draws a batch of cards.
     * @param numCards How many to draw.
     * @return List of drawn cards.
     */
    public List<Card> drawCards(int numCards) {
        if (numCards < 0) throw new IllegalArgumentException("Cannot draw negative number of cards.");
        if (cards.size() < numCards)
            throw new IllegalStateException("Not enough cards to draw " + numCards);
        List<Card> drawn = new ArrayList<>(numCards);
        for (int i = 0; i < numCards; i++) {
            drawn.add(drawCard());
        }
        return drawn;
    }

    /**
     * Adds cards back to the deck (for reshuffling).
     * @param cardsToAdd Cards to reinsert.
     */
    public void addCards(List<Card> cardsToAdd) {
        Objects.requireNonNull(cardsToAdd, "Cannot add null list");
        cards.addAll(cardsToAdd);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int cardsRemaining() {
        return cards.size();
    }

    @Override
    public String toString() {
        return "Deck [" + cardsRemaining() + " cards left]";
    }
}