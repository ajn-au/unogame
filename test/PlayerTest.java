// File: PlayerTest.java
package unogame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // Using Mockito for mocking strategy

/**
 * Unit tests for the Player class.
 */
class PlayerTest {

    private Player player;
    private PlayerStrategy mockStrategy;
    private Hand hand; // Access hand directly for setup

    @BeforeEach
    void setUp() {
        mockStrategy = mock(PlayerStrategy.class); // Create a mock strategy
        player = new Player("Tester", mockStrategy);
        hand = player.getHand(); // Get the player's hand instance
    }

    @Test
    void testPlayerCreation() {
        assertEquals("Tester", player.getName());
        assertNotNull(player.getHand());
        assertTrue(player.getHand().isEmpty());
    }

     @Test
    void testInvalidPlayerCreation() {
         assertThrows(IllegalArgumentException.class, () -> new Player(null, mockStrategy));
         assertThrows(IllegalArgumentException.class, () -> new Player(" ", mockStrategy));
         assertThrows(NullPointerException.class, () -> new Player("Valid", null));
    }

    @Test
    void testChooseCardToPlayDelegatesToStrategy() {
        Game mockGame = mock(Game.class); // Mock Game for context if needed by strategy
        Pile mockPile = mock(Pile.class);
        Card topCard = new NumberCard(Color.RED, Value.FIVE);
        Card cardToPlay = new NumberCard(Color.RED, Value.SEVEN);
        List<Card> playable = Arrays.asList(cardToPlay);

        when(mockGame.getPile()).thenReturn(mockPile);
        when(mockPile.getTopCard()).thenReturn(topCard);
        when(mockGame.getChosenWildColor()).thenReturn(null);
        // Define mock strategy behavior
        when(mockStrategy.chooseCard(any(Hand.class), eq(playable), eq(topCard), eq(null)))
            .thenReturn(cardToPlay);

        Card chosen = player.chooseCardToPlay(mockGame, playable);

        assertEquals(cardToPlay, chosen);
        // Verify that the strategy's method was called exactly once with expected args
        verify(mockStrategy, times(1)).chooseCard(any(Hand.class), eq(playable), eq(topCard), eq(null));
    }

    @Test
    void testChooseWildColorDelegatesToStrategy() {
         Game mockGame = mock(Game.class); // Mock Game context
         Color expectedColor = Color.BLUE;
         // Define mock strategy behavior
         when(mockStrategy.chooseWildColor(any(Hand.class))).thenReturn(expectedColor);

        Color chosen = player.chooseWildColor(mockGame);

        assertEquals(expectedColor, chosen);
        // Verify that the strategy's method was called exactly once
        verify(mockStrategy, times(1)).chooseWildColor(any(Hand.class));
    }

     @Test
     void testEqualsAndHashCode() {
         Player player1a = new Player("Alice", new BasicAIStrategy());
         Player player1b = new Player("Alice", new HumanStrategy(null)); // Different strategy
         Player player2 = new Player("Bob", new BasicAIStrategy());

         assertTrue(player1a.equals(player1b)); // Equal names
         assertTrue(player1b.equals(player1a)); // Symmetric
         assertEquals(player1a.hashCode(), player1b.hashCode()); // Equal hashCodes

         assertFalse(player1a.equals(player2)); // Different names
         assertNotEquals(player1a.hashCode(), player2.hashCode());

         assertFalse(player1a.equals(null));
         assertFalse(player1a.equals("Alice"));
     }
}
