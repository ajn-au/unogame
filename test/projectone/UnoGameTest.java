package projectone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
 * Integration-style tests for the Game class, focusing on core mechanics.
 * Uses fixed deck seeds and simple AI for predictability.
 */
class UnoGameTest {

    private final long fixedSeed = 12345L; // For reproducible deck

    // Simple AI that always plays the first valid card / chooses RED for wild
    /*
    private COMP2033_Project_1_110450836.PlayerStrategy simpleAI = new COMP2033_Project_1_110450836.PlayerStrategy() {
        @Override
        public COMP2033_Project_1_110450836.Card chooseCard(COMP2033_Project_1_110450836.Hand hand, List<COMP2033_Project_1_110450836.Card> playableCards, COMP2033_Project_1_110450836.Card topPileCard, COMP2033_Project_1_110450836.Color chosenWildColor) {
            return playableCards.isEmpty() ? null : playableCards.get(0);
        }
        @Override
        public COMP2033_Project_1_110450836.Color chooseWildColor(COMP2033_Project_1_110450836.Hand hand) {
            return COMP2033_Project_1_110450836.Color.RED;
        }
    };
    */
    List<PlayerStrategy> strategies;
    List<String> playerNames;
    Scanner scanner;

    @BeforeEach
    void setUp() {
        strategies = Arrays.asList(new BasicAIStrategy(), new BasicAIStrategy());
        playerNames = Arrays.asList("Player1", "Player2");
        scanner = new Scanner(System.in);
    }

    @Test
    void testGameInitialization() {

        UnoGame game = new UnoGame(strategies, playerNames, fixedSeed, scanner);
        assertNotNull(game);
        // Further checks could involve peeking at player hands after dealing (needs getter or test setup)
    }

    @Test
    void testInitialDeal() {
         // Need a way to inspect player hands post-deal, maybe a test-specific Game constructor or method
         // Or mock COMP2033_Project_1_110450836.Player/COMP2033_Project_1_110450836.Hand interactions during dealing
         // For now, just ensure it runs without error
         UnoGame game = new UnoGame(strategies, playerNames, fixedSeed, scanner);
         game.run(); // Check if dealing succeeded
         // Cannot easily assert hand sizes without modifying Game/COMP2033_Project_1_110450836.Player for testability
    }

    /*
    @Test
    void testInitialFlipSkip() {
        // Need to control the first card flipped. Mock COMP2033_Project_1_110450836.Deck or inject cards.
        // Mocking COMP2033_Project_1_110450836.Deck:
        COMP2033_Project_1_110450836.UnoGame game = new COMP2033_Project_1_110450836.UnoGame(strategies, playerNames, fixedSeed, scanner);
        game.dealInitialHands();
        // Manually force the pile for testing this specific scenario
        while(game.getPile().getTopCard() == null || game.getPile().getTopCard().getValue() != COMP2033_Project_1_110450836.Value.SKIP) {
           game.getPile().addCard(game.getDeck().drawCard()); // Flip until we get a skip (relies on fixed seed)
           if(game.getDeck().isEmpty()) fail("Could not find a SKIP card with seed " + fixedSeed);
        }
        // System.out.println("Forcing initial card test with: " + game.getPile().getTopCard());
        game.handleInitialCardEffect(game.getPile().getTopCard());
        assertTrue(game.getSkipNextPlayerFlag()); // Need a getter for testing state
        //assertTrue(true);
    }

    /*@Test
    void testTurnAdvance() {
        Game game = new Game(strategies, playerNames, fixedSeed);
        assertEquals(0, game.getCurrentPlayerIndex_TestOnly());
        game.advancePlayer();
        assertEquals(1, game.getCurrentPlayerIndex_TestOnly());
        game.advancePlayer();
        assertEquals(0, game.getCurrentPlayerIndex_TestOnly()); // Wraps around
        game.reversePlayDirection(); // Reverse
        game.advancePlayer();
        assertEquals(1, game.getCurrentPlayerIndex_TestOnly()); // Wraps around backwards
        game.advancePlayer();
        assertEquals(0, game.getCurrentPlayerIndex_TestOnly());
    }*/

    
    // Helper methods in Game might be needed for testing internal state
    // e.g., getCurrentPlayerIndex_TestOnly(), getSkipNextPlayerFlag_TestOnly() etc.
    // These should NOT be part of the production Game API.
}
