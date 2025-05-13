package unogame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests edge cases for deck reshuffling functionality.
 */
public class DeckReshuffleTest {
    private Deck deck;
    private Pile pile;
    private DeckManager deckManager;
    
    @BeforeEach
    void setUp() {
        deck = new Deck();
        pile = new Pile();
        deckManager = new DeckManager(deck, pile);
    }
    
    @ParameterizedTest(name = "Draw {0} cards then reshuffle")
    @ValueSource(ints = {1, 10, 50, 107, 108})
    void testReshuffleAfterDrawingCards(int cardsToDraw) {
        // Draw specified number of cards from deck
        for (int i = 0; i < cardsToDraw; i++) {
            Card card = deck.drawCard();
            pile.addCard(card);
        }
        
        assertEquals(108 - cardsToDraw, deck.cardsLeft());
        assertEquals(cardsToDraw, countCardsInPile(pile));
        
        // Ensure deck not empty (should reshuffle)
        assertTrue(deckManager.ensureDeckNotEmpty());
        
        // After reshuffling, pile should have only the top card
        assertEquals(1, countCardsInPile(pile));
        
        // Deck should have the rest (minus the top card that stays on pile)
        assertEquals(cardsToDraw - 1, deck.cardsLeft());
    }
    
    @Test
    void testCompleteDecimalOddsReshuffleWithAllCards() {
        // Draw all 108 cards
        List<Card> allCards = deck.drawCards(108);
        assertTrue(deck.isEmpty());
        
        // Add all but one to pile (simulating played cards)
        for (int i = 0; i < allCards.size() - 1; i++) {
            pile.addCard(allCards.get(i));
        }
        
        // Ensure deck not empty (should reshuffle)
        assertTrue(deckManager.ensureDeckNotEmpty());
        
        // After reshuffling, pile should have only the top card
        assertEquals(1, countCardsInPile(pile));
        
        // Deck should have the rest
        assertEquals(allCards.size() - 2, deck.cardsLeft());
    }
    
    @Test
    void testReshuffleWithEmptyPile() {
        // Draw all cards
        List<Card> allCards = deck.drawCards(108);
        assertTrue(deck.isEmpty());
        
        // Pile is empty
        assertNull(pile.getTopCard());
        
        // Cannot reshuffle
        assertFalse(deckManager.ensureDeckNotEmpty());
        assertTrue(deck.isEmpty());
    }
    
    @Test
    void testReshuffleWithOnlyOneCardInPile() {
        // Draw all cards
        List<Card> allCards = deck.drawCards(108);
        assertTrue(deck.isEmpty());
        
        // Add 1 card to pile
        pile.addCard(allCards.get(0));
        
        // Trying to reshuffle should return false since only 1 card
        // is in pile and it needs to stay there
        assertFalse(deckManager.ensureDeckNotEmpty());
        assertTrue(deck.isEmpty());
        assertNotNull(pile.getTopCard());
    }
    
    @Test
    void testMultipleReshuffleSequences() {
        int totalCards = 108;
        
        // Draw 50 cards, add to pile
        for (int i = 0; i < 50; i++) {
            Card card = deck.drawCard();
            pile.addCard(card);
        }
        
        // First reshuffle
        assertTrue(deckManager.ensureDeckNotEmpty());
        
        // Draw all but 1 from deck
        deck.drawCards(deck.cardsLeft() - 1);
        
        // Second reshuffle
        assertTrue(deckManager.ensureDeckNotEmpty());
        
        // Draw all but 1 again
        deck.drawCards(deck.cardsLeft() - 1);
        
        // Third reshuffle
        assertTrue(deckManager.ensureDeckNotEmpty());
        
        // Draw all cards
        deck.drawCards(deck.cardsLeft());
        
        // Should no longer be able to reshuffle
        assertFalse(deckManager.ensureDeckNotEmpty());
    }
    
    /**
     * Helper method to count cards in pile through reshuffling
     */
    private int countCardsInPile(Pile pile) {
        Card topCard = pile.getTopCard();
        List<Card> others = pile.takeCardsForReshuffle();
        int count = (topCard != null ? 1 : 0) + others.size();
        
        // Restore pile state
        for (Card card : others) {
            pile.addCard(card);
        }
        if (topCard != null) {
            pile.addCard(topCard);
        }
        
        return count;
    }
}
