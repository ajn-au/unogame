package projectone;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;


/**
 * Unit tests for the projectone.Deck class.
 */
class DeckTest {

    @Test
    void testDeckInitializationSize() {
        Deck deck = new Deck();
        assertEquals(108, deck.cardsRemaining(), "Standard deck should have 108 cards initially.");
        assertFalse(deck.isEmpty());
    }

    @Test
    void testDrawCardReducesSize() {
        Deck deck = new Deck();
        Card drawn = deck.drawCard();
        assertNotNull(drawn);
        assertEquals(107, deck.cardsRemaining(), "Drawn card should reduce size by 1.");
    }

    @Test
    void testDrawUntilEmpty() {
        Deck deck = new Deck();
        for (int i = 0; i < 108; i++) {
            assertFalse(deck.isEmpty());
            assertNotNull(deck.drawCard());
        }
        assertTrue(deck.isEmpty());
        assertEquals(0, deck.cardsRemaining());
    }

    @Test
    void testDrawFromEmptyDeckThrowsException() {
        Deck deck = new Deck();
        // Draw all cards
        deck.drawCards(108);
        assertTrue(deck.isEmpty());
        // Attempt to draw one more
        assertThrows(IllegalStateException.class, deck::drawCard);
    }

     @Test
     void testDrawCardsMultiple() {
         Deck deck = new Deck();
         List<Card> drawn = deck.drawCards(7);
         assertEquals(7, drawn.size());
         assertEquals(101, deck.cardsRemaining());
     }

     @Test
     void testDrawCardsInsufficientThrowsException() {
         Deck deck = new Deck();
         deck.drawCards(100); // Leave 8 cards
         assertThrows(IllegalStateException.class, () -> deck.drawCards(10));
     }

      @Test
     void testDrawNegativeCardsThrowsException() {
         Deck deck = new Deck();
         assertThrows(IllegalArgumentException.class, () -> deck.drawCards(-1));
     }


    @Test
    void testShuffleChangesOrder() {
        long seed = 12345L;
        Deck deck1 = new Deck(seed);
        Deck deck2 = new Deck(seed); // Same seed

        // Draw a few cards and compare - they should be the same initially
        Card card1_d1 = deck1.drawCard();
        Card card1_d2 = deck2.drawCard();
        assertEquals(card1_d1, card1_d2, "Decks with same seed should yield same cards initially");

        // Shuffle deck1
        deck1.shuffle();

        // Draw again - now they should likely be different
        Card card2_d1 = deck1.drawCard();
        Card card2_d2 = deck2.drawCard(); // projectone.Deck 2 not shuffled
        // It's *possible* they are the same by chance, but highly unlikely with a full deck
        assertNotEquals(card2_d1, card2_d2, "Shuffled deck should likely yield different card than unshuffled");

        // Ensure all original cards are still present after shuffle (though order changed)
         Deck deck3 = new Deck(seed); // Fresh deck with same seed
         List<Card> originalCards = deck3.drawCards(108); // Get all cards in original order
         List<Card> shuffledCards = deck1.drawCards(106); // Get remaining shuffled cards
         shuffledCards.add(card2_d1); // Add the one we drew earlier
         shuffledCards.add(card1_d1); // Add the very first one

         Set<Card> originalSet = new HashSet<>(originalCards);
         Set<Card> shuffledSet = new HashSet<>(shuffledCards);

         assertEquals(54, shuffledSet.size(), "Shuffled deck should still contain unique cards if setup correctly");
         assertEquals(originalSet, shuffledSet, "Shuffled deck must contain the exact same set of cards as the original");
    }

     @Test
     void testAddCards() {
         Deck deck = new Deck();
         List<Card> drawn = deck.drawCards(10);
         assertEquals(98, deck.cardsRemaining());

         deck.addCards(drawn);
         assertEquals(108, deck.cardsRemaining());
         // Note: addCards doesn't reshuffle.
     }

     @Test
     void testToString() {
         Deck deck = new Deck();
         String deckString = deck.toString();
         assertTrue(deckString.contains("108 cards left"), "toString should indicate number of cards");
     }
}
