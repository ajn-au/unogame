package projectone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the COMP2033_Project_1_110450836.Player class.
 */
class PlayerTest {

    private Player player;
    private PlayerStrategy mockStrategy;

    @BeforeEach
    void setUp() {
        mockStrategy = new BasicAIStrategy();
        player = new Player("Tester", mockStrategy);
    }


    @Test
    void testPlayerCreation() {
        assertEquals("Tester", player.getName());
        assertNotNull(player.getHand());
        assertTrue(player.getHand().isEmpty());
    }

     @Test
    void testInvalidPlayerCreation() {
         assertThrows(NullPointerException.class, () -> new Player(null, mockStrategy));
         assertThrows(IllegalArgumentException.class, () -> new Player(" ", mockStrategy));
         assertThrows(IllegalArgumentException.class, () -> new Player("", mockStrategy));
         //assertThrows(NullPointerException.class, () -> new COMP2033_Project_1_110450836.Player("Valid", null));
    }

     @Test
     void testEqualsAndHashCode() {
         Player player1a = new Player("Alice", new BasicAIStrategy());
         Player player1b = new Player("Alice", new HumanStrategy(new Scanner(System.in))); // Different strategy
         Player player2 = new Player("Bob", new BasicAIStrategy());
         
         assertTrue(player1a.equals(player1b)); // Equal names
         assertTrue(player1b.equals(player1a)); // Symmetric
         assertEquals(player1a.hashCode(), player1b.hashCode()); // Equal hashCodes

         assertFalse(player1a.equals(player2)); // Different names
         assertNotEquals(player1a.hashCode(), player2.hashCode());

         assertFalse(player1a.equals(null));
         assertFalse(player1a.equals(player1b));
     }
}
