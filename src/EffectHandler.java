package unogame;

/**
 * Handles the application of card effects in an Uno game.
 * Centralizes effect logic to keep the main game class cleaner.
 *
  * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tools Used: None
 * This is my own work as defined by the University's Academic Integrity Policy.
 */
public class EffectHandler {
    private final GameStateManager gameState;
    private final DeckManager deckManager;
    
    /**
     * Creates a new effect handler.
     * @param gameState The game state manager
     * @param deckManager The deck manager
     */
    public EffectHandler(GameStateManager gameState, DeckManager deckManager) {
        this.gameState = gameState;
        this.deckManager = deckManager;
    }
    
    /**
     * Applies the effect of a card.
     * @param card The card to apply
     * @param playedBy The player who played the card
     */
    public void applyCardEffect(Card card, Player playedBy) {
        switch (card.getValue()) {
            case SKIP:
                applySkipEffect();
                break;
            case REVERSE:
                applyReverseEffect();
                break;
            case DRAW_TWO:
                applyDrawTwoEffect();
                break;
            case WILD:
                applyWildEffect(playedBy);
                break;
            case WILD_DRAW_FOUR:
                applyWildDrawFourEffect(playedBy);
                break;
            default:
                // No effect for number cards
                break;
        }
    }
    
    /**
     * Applies the SKIP card effect.
     */
    private void applySkipEffect() {
        gameState.skipNextPlayerTurn();
        System.out.println(gameState.getNextPlayer().getName() + "'s turn will be skipped!");
    }
    
    /**
     * Applies the REVERSE card effect.
     */
    private void applyReverseEffect() {
        gameState.reversePlayDirection();
        System.out.println("Play direction reversed to " + 
                         (gameState.isPlayDirectionClockwise() ? "clockwise" : "counter-clockwise"));
        
        // In a two-player game, REVERSE acts like SKIP
        if (gameState.getNumberOfPlayers() == 2) {
            gameState.skipNextPlayerTurn();
            System.out.println(gameState.getNextPlayer().getName() + "'s turn will be skipped!");
        }
    }
    
    /**
     * Applies the DRAW_TWO card effect.
     */
    private void applyDrawTwoEffect() {
        Player nextPlayer = gameState.getNextPlayer();
        int cardsDrawn = deckManager.makePlayerDraw(nextPlayer, 2);
        System.out.println(nextPlayer.getName() + " draws " + cardsDrawn + " cards and is skipped!");
        gameState.skipNextPlayerTurn();
    }
    
    /**
     * Applies the WILD card effect.
     * @param player The player who played the wild
     */
    private void applyWildEffect(Player player) {
        Color chosenColor = player.chooseWildColor(gameState);
        gameState.setActiveWildColor(chosenColor);
        System.out.println(player.getName() + " chose " + chosenColor + " as the new color!");
    }
    
    /**
     * Applies the WILD_DRAW_FOUR card effect.
     * @param player The player who played the wild draw four
     */
    private void applyWildDrawFourEffect(Player player) {
        // Choose color first (same as regular wild)
        applyWildEffect(player);
        
        // Then make next player draw four cards and skip their turn
        Player nextPlayer = gameState.getNextPlayer();
        int cardsDrawn = deckManager.makePlayerDraw(nextPlayer, 4);
        System.out.println(nextPlayer.getName() + " draws " + cardsDrawn + " cards and is skipped!");
        gameState.skipNextPlayerTurn();
    }
}
