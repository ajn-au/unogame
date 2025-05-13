// File: HandTest.java
package unogame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Hand class focusing on sorting behavior (using ArrayList)
 * and valid card finding.
 */
class HandTest {
    private Hand hand;
    private Card redTwo;
    private Card redFour;
    private Card blueTwo;
    private Card blueSkip;
    private Card yellowSkip;
    private Card wild;
    private Card wildD4;

    @BeforeEach
    void setUp() {
        hand = new Hand();
        redTwo = new NumberCard(Color.RED, Value.TWO);
        redFour = new NumberCard(Color.RED, Value.FOUR);
        blueTwo = new NumberCard(Color.BLUE, Value.TWO);
        blueSkip = new ActionCard(Color.BLUE, Value.SKIP);
        yellowSkip = new ActionCard(Color.YELLOW, Value.SKIP);
        wild = new WildCard(Value.WILD);
        wildD4 = new WildCard(Value.WILD_DRAW_FOUR);
    }

    @Test
    void testEmptyHand() {
        assertTrue(hand.isEmpty());
        assertEquals(0, hand.getSize());
        assertTrue(hand.getCards().isEmpty());
        assertEquals("Hand: []", hand.toString());
    }

    @Test
    void testAddCardMaintainsSort() {
        hand.addCard(wild);
        hand.addCard(blueTwo);
        hand.addCard(redTwo); // Added out of order

        List<Card> cards = hand.getCards();
        assertEquals(3, cards.size());
        assertEquals(redTwo, cards.get(0)); // Red first
        assertEquals(blueTwo, cards.get(1)); // Then Blue
        assertEquals(wild, cards.get(2));   // Then Wild
    }

    @Test
    void testAddCardsMaintainsSort() {
        List<Card> toAdd = Arrays.asList(wild, blueTwo, redTwo, yellowSkip, redFour);
        hand.addCards(toAdd);

        List<Card> cards = hand.getCards();
        assertEquals(5, cards.size());
        // Expected: R2, R4, YS, B2, W
        assertEquals(redTwo, cards.get(0));
        assertEquals(redFour, cards.get(1));
        assertEquals(yellowSkip, cards.get(2));
        assertEquals(blueTwo, cards.get(3));
        assertEquals(wild, cards.get(4));
    }

    @Test
    void testRemoveCardObject() {
        hand.addCards(Arrays.asList(redTwo, blueTwo, wild));
        assertTrue(hand.removeCard(blueTwo));
        assertEquals(2, hand.getSize());
        assertFalse(hand.getCards().contains(blueTwo));
        assertEquals(redTwo, hand.getCards().get(0)); // Check remaining order
        assertEquals(wild, hand.getCards().get(1));

        assertFalse(hand.removeCard(blueSkip)); // Card not in hand
        assertEquals(2, hand.getSize());
    }

    @Test
    void testPlayCardObject() {
        hand.addCards(Arrays.asList(redTwo, blueTwo, wild));
        Card played = hand.playCard(blueTwo);
        assertEquals(blueTwo, played);
        assertEquals(2, hand.getSize());
        assertFalse(hand.getCards().contains(blueTwo));
        assertEquals(redTwo, hand.getCards().get(0));
        assertEquals(wild, hand.getCards().get(1));
    }

     @Test
    void testPlayCardObjectNotFound() {
        hand.addCards(Arrays.asList(redTwo, wild));
         assertThrows(IllegalArgumentException.class, () -> hand.playCard(blueTwo));
         assertEquals(2, hand.getSize()); // Hand unchanged
    }

     @Test
    void testPlayCardIndex() {
        hand.addCards(Arrays.asList(redTwo, redFour, blueTwo, yellowSkip, wild));
        // Expected order: R2[0], R4[1], B2[2], YS[3], W[4]
        Card played = hand.playCard(2); // Play blueTwo
        assertEquals(blueTwo, played);
        assertEquals(4, hand.getSize());
        List<Card> remaining = hand.getCards();
        assertEquals(redTwo, remaining.get(0));
        assertEquals(redFour, remaining.get(1));
        assertEquals(yellowSkip, remaining.get(2));
        assertEquals(wild, remaining.get(3));
    }

     @Test
    void testPlayCardIndexOutOfBounds() {
        hand.addCards(Arrays.asList(redTwo, wild));
         assertThrows(IndexOutOfBoundsException.class, () -> hand.playCard(2));
         assertThrows(IndexOutOfBoundsException.class, () -> hand.playCard(-1));
    }


    @Test
    void testFindValidCardsSimple() {
        hand.addCards(Arrays.asList(redTwo, blueTwo, wild, yellowSkip));
        // Pile top is BLUE SKIP
        List<Card> valid = hand.findValidCards(blueSkip, null);
        assertEquals(2, valid.size());
        assertTrue(valid.contains(blueTwo)); // Matches color
        assertTrue(valid.contains(wild));     // Wild is always valid
        assertFalse(valid.contains(yellowSkip));
         assertFalse(valid.contains(redTwo));
    }

     @Test
    void testFindValidCardsOnWild() {
        hand.addCards(Arrays.asList(redTwo, redFour, blueTwo, yellowSkip, wild));
        // Pile top is WILD, chosen color is RED
        List<Card> valid = hand.findValidCards(wild, Color.RED);
         // Should be red cards + any wilds
        assertEquals(3, valid.size());
        assertTrue(valid.contains(redTwo));
        assertTrue(valid.contains(redFour));
        assertTrue(valid.contains(wild)); // The wild card in hand
        assertFalse(valid.contains(blueTwo));
        assertFalse(valid.contains(yellowSkip));
    }

     @Test
    void testFindValidCardsNone() {
        hand.addCards(Arrays.asList(redTwo, redFour));
        // Pile top is BLUE SKIP
        List<Card> valid = hand.findValidCards(blueSkip, null);
        assertTrue(valid.isEmpty());
    }

    @Test
    void testGetCardsIsUnmodifiable() {
        hand.addCard(redTwo);
        List<Card> cardsView = hand.getCards();
        assertThrows(UnsupportedOperationException.class, () -> cardsView.add(blueTwo));
        assertThrows(UnsupportedOperationException.class, () -> cardsView.remove(0));
        assertThrows(UnsupportedOperationException.class, () -> cardsView.clear());
    }

     @Test
    void testToString() {
        hand.addCards(Arrays.asList(wild, blueTwo, redTwo));
        assertEquals("Hand: [RED TWO, BLUE TWO, WILD WILD]", hand.toString());
    }
}
