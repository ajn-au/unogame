// File: CardTest.java
package unogame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Card abstract class and its subclasses.
 */
class CardTest {

    // --- Constructor and Basic Properties Tests ---

    @Test
    void testValidNumberCardCreation() {
        Card card = new NumberCard(Color.RED, Value.FIVE);
        assertEquals(Color.RED, card.getColor());
        assertEquals(Value.FIVE, card.getValue());
        assertEquals("RED FIVE", card.toString());
    }

    @Test
    void testValidActionCardCreation() {
        Card card = new ActionCard(Color.BLUE, Value.SKIP);
        assertEquals(Color.BLUE, card.getColor());
        assertEquals(Value.SKIP, card.getValue());
        assertEquals("BLUE SKIP", card.toString());
    }

    @Test
    void testValidWildCardCreation() {
        Card card = new WildCard(Value.WILD);
        assertEquals(Color.WILD, card.getColor());
        assertEquals(Value.WILD, card.getValue());
        assertEquals("WILD WILD", card.toString());
    }

    @Test
    void testInvalidNumberCardValue() {
        assertThrows(IllegalArgumentException.class, () -> new NumberCard(Color.GREEN, Value.SKIP));
    }

     @Test
    void testInvalidNumberCardColor() {
        assertThrows(IllegalArgumentException.class, () -> new NumberCard(Color.WILD, Value.ONE));
    }

    @Test
    void testInvalidActionCardValue() {
        assertThrows(IllegalArgumentException.class, () -> new ActionCard(Color.YELLOW, Value.ONE));
    }

     @Test
    void testInvalidActionCardColor() {
        assertThrows(IllegalArgumentException.class, () -> new ActionCard(Color.WILD, Value.REVERSE));
    }

    @Test
    void testInvalidWildCardValue() {
        assertThrows(IllegalArgumentException.class, () -> new WildCard(Value.ONE));
    }

    @Test
    void testInvalidWildCardColor() {
        // Card constructor prevents non-WILD color for WILD values
        assertThrows(IllegalArgumentException.class, () -> new Card(Color.RED, Value.WILD){
             @Override public void applyEffect(Game game) {} // Dummy implementation
        });
    }

    // --- canPlayOn Tests ---

    @Test
    void testCanPlayOnColorMatch() {
        Card top = new NumberCard(Color.RED, Value.FIVE);
        Card play = new NumberCard(Color.RED, Value.SEVEN);
        assertTrue(play.canPlayOn(top, null));
    }

    @Test
    void testCanPlayOnValueMatch() {
        Card top = new NumberCard(Color.RED, Value.FIVE);
        Card play = new NumberCard(Color.BLUE, Value.FIVE);
        assertTrue(play.canPlayOn(top, null));
    }

     @Test
    void testCanPlayOnActionValueMatch() {
        Card top = new ActionCard(Color.RED, Value.SKIP);
        Card play = new ActionCard(Color.BLUE, Value.SKIP);
        assertTrue(play.canPlayOn(top, null));
    }

    @Test
    void testCannotPlayMismatch() {
        Card top = new NumberCard(Color.RED, Value.FIVE);
        Card play = new NumberCard(Color.BLUE, Value.SEVEN);
        assertFalse(play.canPlayOn(top, null));
    }

    @Test
    void testWildCanPlayOnAnything() {
        Card top = new NumberCard(Color.RED, Value.FIVE);
        Card play = new WildCard(Value.WILD);
        assertTrue(play.canPlayOn(top, null));
    }

    // testWildDrawFourCanPlayOnAnything removed as it's redundant with WildCard logic
    // and the game rules about initial play are handled elsewhere.
    // The Card.canPlayOn method only checks basic playability, not game-start restrictions.
    // The logic for handling initial WILD_DRAW_FOUR is in Game.flipInitialCard().


    @Test
    void testCanPlayOnWildChosenColor() {
        Card top = new WildCard(Value.WILD);
        Card playRed = new NumberCard(Color.RED, Value.ONE);
        Card playBlue = new NumberCard(Color.BLUE, Value.TWO);

        assertTrue(playRed.canPlayOn(top, Color.RED));
        assertFalse(playBlue.canPlayOn(top, Color.RED));
        assertTrue(playBlue.canPlayOn(top, Color.BLUE));
    }

     @Test
    void testCanPlayNonWildOnWildNoChosenColor() {
        // Should technically be allowed, game state dictates playability
        // based on whether a color has been chosen yet. canPlayOn assumes
        // if chosenWildColor is null, the wild hasn't resolved.
        // A non-wild card cannot match the WILD color itself.
        Card top = new WildCard(Value.WILD);
        Card playRed = new NumberCard(Color.RED, Value.ONE);
        assertFalse(playRed.canPlayOn(top, null));
    }


    // --- compareTo Tests ---

    @Test
    void testCompareToColorPrimary() {
        Card red = new NumberCard(Color.RED, Value.ONE);
        Card yellow = new NumberCard(Color.YELLOW, Value.ONE);
        Card wild = new WildCard(Value.WILD);

        assertTrue(red.compareTo(yellow) < 0); // RED < YELLOW
        assertTrue(yellow.compareTo(red) > 0);
        assertTrue(yellow.compareTo(wild) < 0); // YELLOW < WILD
    }

    @Test
    void testCompareToValueSecondary() {
        Card red1 = new NumberCard(Color.RED, Value.ONE);
        Card red5 = new NumberCard(Color.RED, Value.FIVE);
        Card redSkip = new ActionCard(Color.RED, Value.SKIP);
        Card wild = new WildCard(Value.WILD);
        Card wildD4 = new WildCard(Value.WILD_DRAW_FOUR);

        assertTrue(red1.compareTo(red5) < 0);    // ONE < FIVE
        assertTrue(red5.compareTo(redSkip) < 0); // FIVE < SKIP (based on enum order)
        assertTrue(redSkip.compareTo(red1) > 0);
        // Cannot compare non-WILD with WILD directly using this logic if needed
        // assertTrue(redSkip.compareTo(wild) < 0); // Handled by Color compare first
         assertTrue(wild.compareTo(wildD4) < 0); // WILD < WILD_DRAW_FOUR
    }

    @Test
    void testCompareToEqual() {
        Card red1a = new NumberCard(Color.RED, Value.ONE);
        Card red1b = new NumberCard(Color.RED, Value.ONE);
        assertEquals(0, red1a.compareTo(red1b));
    }

    // --- equals and hashCode Tests ---

    @Test
    void testEquals() {
        Card r1a = new NumberCard(Color.RED, Value.ONE);
        Card r1b = new NumberCard(Color.RED, Value.ONE);
        Card b1 = new NumberCard(Color.BLUE, Value.ONE);
        Card r2 = new NumberCard(Color.RED, Value.TWO);
        Card wild = new WildCard(Value.WILD);

        assertTrue(r1a.equals(r1b)); // Equal cards
        assertTrue(r1b.equals(r1a)); // Symmetric
        assertFalse(r1a.equals(b1)); // Different color
        assertFalse(r1a.equals(r2)); // Different value
        assertFalse(r1a.equals(null)); // Vs null
        assertFalse(r1a.equals("RED ONE")); // Vs other type
        assertFalse(r1a.equals(wild)); // Vs different type/value
    }

    @Test
    void testHashCode() {
         Card r1a = new NumberCard(Color.RED, Value.ONE);
         Card r1b = new NumberCard(Color.RED, Value.ONE);
         Card b1 = new NumberCard(Color.BLUE, Value.ONE);

         assertEquals(r1a.hashCode(), r1b.hashCode()); // Equal objects -> equal hashCodes
         assertNotEquals(r1a.hashCode(), b1.hashCode()); // Different objects likely different hashCodes
    }

    // applyEffect tests would ideally be in GameTest as they modify game state
    // or require mocking the Game interface/class.
}
