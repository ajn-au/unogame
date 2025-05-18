package projectone;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

/**
 * Parameterized tests for card matching logic.
 * Tests various combinations of cards, colors, and wild cases.
 */
public class CardMatchingParameterizedTest {

    @ParameterizedTest(name = "{index}: {0} on {1} with wild color {2} should be {3}")
    @MethodSource("provideCardMatchingScenarios")
    void testCardMatching(Card cardToPlay, Card topCard, Color activeWildColor, boolean expected) {
        assertEquals(expected, cardToPlay.canPlayOn(topCard, activeWildColor));
    }
    
    private static Stream<Arguments> provideCardMatchingScenarios() {
        // Create all test cards
        Card redOne = new NumberCard(Color.RED, Value.ONE);
        Card redFive = new NumberCard(Color.RED, Value.FIVE);
        Card redSkip = new ActionCard(Color.RED, Value.SKIP);
        Card redDrawTwo = new ActionCard(Color.RED, Value.DRAW_TWO);
        
        Card blueOne = new NumberCard(Color.BLUE, Value.ONE);
        Card blueFive = new NumberCard(Color.BLUE, Value.FIVE);
        Card blueSkip = new ActionCard(Color.BLUE, Value.SKIP);
        
        Card yellowFive = new NumberCard(Color.YELLOW, Value.FIVE);
        Card greenDrawTwo = new ActionCard(Color.GREEN, Value.DRAW_TWO);
        
        Card wild = new WildCard(Color.WILD, Value.WILD);
        Card wildDrawFour = new WildCard(Color.WILD, Value.WILD_DRAW_FOUR);

        // Return scenarios as Stream<Arguments>
        return Stream.of(
            // Same color matching scenarios
            Arguments.of(redFive, redOne, null, true),
            Arguments.of(blueSkip, blueOne, null, true),
            
            // Same value matching scenarios
            Arguments.of(blueFive, redFive, null, true),
            Arguments.of(blueSkip, redSkip, null, true),
            
            // No match scenarios
            Arguments.of(blueFive, redOne, null, false),
            Arguments.of(yellowFive, blueSkip, null, false),
            
            // Wild cards can always be played
            Arguments.of(wild, redOne, null, true),
            Arguments.of(wild, blueSkip, null, true),
            Arguments.of(wildDrawFour, greenDrawTwo, null, true),
            
            // Playing on wild cards with active color
            Arguments.of(redOne, wild, Color.RED, true),
            Arguments.of(blueOne, wild, Color.RED, false),
            Arguments.of(blueOne, wild, Color.BLUE, true),
            Arguments.of(blueSkip, wild, Color.BLUE, true),
            
            // Playing on wild cards without active color set
            Arguments.of(redOne, wild, null, false),
            Arguments.of(wild, wild, null, true), // Wild can always be played
            
            // Special case: draw two matching
            Arguments.of(redDrawTwo, greenDrawTwo, null, true),
            
            // Edge cases
            Arguments.of(wild, wildDrawFour, Color.RED, true),
            Arguments.of(redOne, wildDrawFour, Color.RED, true),
            Arguments.of(wildDrawFour, wild, null, true)
        );
    }
    
    @ParameterizedTest(name = "{index}: COMP2033_Project_1_110450836.Card with color {0} and value {1} should be {2}")
    @MethodSource("provideCardValidationScenarios")
    void testCardValidation(Color color, Value value, boolean shouldBeValid) {
        if (shouldBeValid) {
            // Should create without exception
            Card card = createCard(color, value);
            assertEquals(color, card.getColor());
            assertEquals(value, card.getValue());
        } else {
            // Should throw exception
            assertThrows(IllegalArgumentException.class, () -> createCard(color, value));
        }
    }
    
    private static Card createCard(Color color, Value value) {
        if (color == Color.WILD || value == Value.WILD || value == Value.WILD_DRAW_FOUR) {
            return new WildCard(color,value);
        } else if (value.ordinal() <= Value.NINE.ordinal()) {
            return new NumberCard(color, value);
        } else {
            return new ActionCard(color, value);
        }
    }
    
    private static Stream<Arguments> provideCardValidationScenarios() {
        return Stream.of(
            // Valid cards
            Arguments.of(Color.RED, Value.ZERO, true),
            Arguments.of(Color.BLUE, Value.FIVE, true),
            Arguments.of(Color.GREEN, Value.NINE, true),
            Arguments.of(Color.YELLOW, Value.SKIP, true),
            Arguments.of(Color.RED, Value.REVERSE, true),
            Arguments.of(Color.BLUE, Value.DRAW_TWO, true),
            Arguments.of(Color.WILD, Value.WILD, true),
            Arguments.of(Color.WILD, Value.WILD_DRAW_FOUR, true),
            
            // Invalid cards
            Arguments.of(Color.RED, Value.WILD, false),
            Arguments.of(Color.BLUE, Value.WILD_DRAW_FOUR, false),
            Arguments.of(Color.WILD, Value.ONE, false),
            Arguments.of(Color.WILD, Value.SKIP, false)
        );
    }
}
