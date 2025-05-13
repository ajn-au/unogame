package unogame;

import java.util.List;
import java.util.Objects;

/**
 * Manages interactions between the deck and pile in an Uno game.
 * Handles card drawing, reshuffling, and initial setup.
 * 
  * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tools Used: None
 * This is my own work as defined by the University's Academic Integrity Policy.
 */
public class DeckManager {
    private final Deck deck;
    private final Pile pile;
    
    /**
     * Creates a new deck manager with the specified deck and pile.
     * @param deck The game deck
     * @param pile The discard pile
     */
    public DeckManager(Deck deck, Pile pile) {
        this.deck = Objects.requireNonNull(deck, "Deck cannot be null");
        this.pile = Objects.requireNonNull(pile, "Pile cannot be null");
    }
    
    /**
     * Ensures the deck has at least one card, reshuffling from the pile if necessary.
     * @return true if deck has or was restocked with at least one card, false otherwise
     */
    public boolean ensureDeckNotEmpty() {
        if (!deck.isEmpty()) {
            return true;
        }
        
        List<Card> cardsToReshuffle = pile.takeCardsForReshuffle();
        if (cardsToReshuffle.isEmpty()) {
            return false;
        }
        
        deck.addCards(cardsToReshuffle);
        deck.shuffle();
        return true;
    }
    
    /**
     * Draws a card from the deck, reshuffling if necessary.
     * @return The drawn card, or null if no cards are available
     */
    public Card drawCard() {
        if (!ensureDeckNotEmpty()) {
            return null;
        }
        return deck.drawCard();
    }
    
    /**
     * Makes a player draw a specified number of cards.
     * @param player The player to draw cards
     * @param count Number of cards to draw
     * @return Number of cards actually drawn (may be less than requested if deck runs out)
     */
    public int makePlayerDraw(Player player, int count) {
        if (count <= 0) return 0;
        
        int cardsDrawn = 0;
        for (int i = 0; i < count; i++) {
            Card drawn = drawCard();
            if (drawn == null) {
                break;
            }
            player.getHand().addCard(drawn);
            cardsDrawn++;
        }
        return cardsDrawn;
    }
    
    /**
     * Deals initial hands to all players.
     * @param players List of players
     * @param cardsPerPlayer Number of cards to deal to each player
     * @return true if dealing succeeded, false if ran out of cards
     */
    public boolean dealInitialHands(List<Player> players, int cardsPerPlayer) {
        for (Player player : players) {
            try {
                List<Card> cards = deck.drawCards(cardsPerPlayer);
                player.getHand().addCards(cards);
            } catch (IllegalStateException e) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Flips the initial card onto the pile.
     * @return The flipped card, or null if no valid card could be flipped
     */
    public Card flipInitialCard() {
        while (true) {
            if (!ensureDeckNotEmpty()) {
                return null;
            }
            
            Card firstCard = deck.drawCard();
            
            // WILD_DRAW_FOUR cannot be the starting card per UNO rules
            if (firstCard.getValue() == Value.WILD_DRAW_FOUR) {
                // Return to bottom of deck and try again
                deck.addCards(List.of(firstCard));
                deck.shuffle();
                continue;
            }
            
            pile.addCard(firstCard);
            return firstCard;
        }
    }
    
    /**
     * Gets the current top card on the pile.
     * @return The top card
     */
    public Card getTopPileCard() {
        return pile.getTopCard();
    }
    
    /**
     * Adds a card to the pile.
     * @param card The card to add
     */
    public void addCardToPile(Card card) {
        pile.addCard(card);
    }
    
    /**
     * Gets the number of cards left in the deck.
     * @return Card count
     */
    public int getCardsLeftInDeck() {
        return deck.cardsLeft();
    }
}
