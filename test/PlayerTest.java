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

    @BeforeEach
    void setUp() {
        mockStrategy = mock(PlayerStrategy.class); // Create a mock strategy
        player = new Player("Tester");
    }


    @Test
    void testPlayerCreation() {
        assertEquals("Tester", player.getName());
        assertNotNull(player.getHand());
        assertTrue(player.getHand().isEmpty());
    }

     @Test
    void testInvalidPlayerCreation() {
         assertThrows(IllegalArgumentException.class, () -> new Player(null));
         assertThrows(IllegalArgumentException.class, () -> new Player(" "));
         assertThrows(IllegalArgumentException.class, () -> new Player(""));
         //assertThrows(NullPointerException.class, () -> new Player("Valid", null));
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
