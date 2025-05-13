// File: PileTest.java
package unogame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Pile class.
 */
class PileTest {
    private Pile pile;
    private Card redTwo;
    private Card blueThree;
    private Card wild;

    @BeforeEach
    void setUp() {
        pile = new Pile();
        redTwo = new NumberCard(Color.RED, Value.TWO);
        blueThree = new NumberCard(Color.BLUE, Value.THREE);
        wild = new WildCard(Value.WILD);
    }

    @Test
    void testInitialPileIsEmpty() {
        assertNull(pile.getTopCard());
    }

    @Test
    void testAddCard() {
        pile.addCard(redTwo);
        assertEquals(redTwo, pile.getTopCard());
        pile.addCard(blueThree);
        assertEquals(blueThree, pile.getTopCard()); // LIFO order
    }

    @Test
    void testAddNullCardThrowsException() {
         assertThrows(IllegalArgumentException.class, () -> pile.addCard(null));
    }

    @Test
    void testGetTopCardDoesNotRemove() {
        pile.addCard(redTwo);
        assertEquals(redTwo, pile.getTopCard());
        assertEquals(redTwo, pile.getTopCard()); // Calling again yields same card
    }

    @Test
    void testTakeCardsForReshuffleEmpty() {
        List<Card> reshuffle = pile.takeCardsForReshuffle();
        assertTrue(reshuffle.isEmpty());
        assertNull(pile.getTopCard());
    }

    @Test
    void testTakeCardsForReshuffleOneCard() {
        pile.addCard(redTwo);
        List<Card> reshuffle = pile.takeCardsForReshuffle();
        assertTrue(reshuffle.isEmpty());
        assertEquals(redTwo, pile.getTopCard()); // Pile still has the one card
    }

     @Test
    void testTakeCardsForReshuffleMultipleCards() {
        pile.addCard(redTwo);
        pile.addCard(blueThree); // blueThree is top
        pile.addCard(wild);      // wild is top

        assertEquals(wild, pile.getTopCard());

        List<Card> reshuffle = pile.takeCardsForReshuffle();

        assertEquals(2, reshuffle.size());
        assertTrue(reshuffle.contains(blueThree));
        assertTrue(reshuffle.contains(redTwo));
        assertFalse(reshuffle.contains(wild)); // Top card not included

        // Pile should only contain the original top card now
        assertEquals(wild, pile.getTopCard());
        // A bit tricky to check size directly on Deque, but peek should be reliable
        // Try adding another card to see if it becomes the new top
        Card greenSkip = new ActionCard(Color.GREEN, Value.SKIP);
        pile.addCard(greenSkip);
        assertEquals(greenSkip, pile.getTopCard());
        // Now try reshuffling again, should only get 'wild'
         List<Card> reshuffle2 = pile.takeCardsForReshuffle();
         assertEquals(1, reshuffle2.size());
         assertEquals(wild, reshuffle2.get(0));
         assertEquals(greenSkip, pile.getTopCard()); // Pile left with greenSkip

    }

    @Test
    void testCanPlayDelegation() {
        pile.addCard(redTwo); // Top is Red Two
        Card redFive = new NumberCard(Color.RED, Value.FIVE);
        Card blueTwo = new NumberCard(Color.BLUE, Value.TWO);
        Card greenThree = new NumberCard(Color.GREEN, Value.THREE);
        Card wildCard = new WildCard(Value.WILD);

        assertTrue(pile.canPlay(redFive, null)); // Color match
        assertTrue(pile.canPlay(blueTwo, null));  // Value match
        assertFalse(pile.canPlay(greenThree, null)); // No match
        assertTrue(pile.canPlay(wildCard, null));    // Wild match
    }

     @Test
    void testCanPlayOnWildDelegation() {
        pile.addCard(wild); // Top is Wild
        Card redFive = new NumberCard(Color.RED, Value.FIVE);

        assertTrue(pile.canPlay(redFive, Color.RED)); // Chosen color matches
        assertFalse(pile.canPlay(redFive, Color.BLUE)); // Chosen color doesn't match
        // assertFalse(pile.canPlay(redFive, null)); // No chosen color, cannot play non-wild
    }
