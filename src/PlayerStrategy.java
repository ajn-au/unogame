import java.util.List;

/**
 * Strategy interface for deciding how a player selects a card and wild color.
 * Allows for both human and AI player implementations.
 */
public interface PlayerStrategy {
    /**
     * Chooses a card to play based on game state and hand.
     *
     * @param gameController Provides context including top pile card and current wild color.
     * @param hand The player's hand.
     * @param playableCards Cards filtered by hand as valid to play.
     * @param topPileCard The card currently on top of the discard pile.
     * @param activeWildColor The currently selected wild color.
     * @return Chosen card or null to pass.
     */
    Card chooseCard(UnoGame gameController, Hand hand, List<Card> playableCards, Card topPileCard, Color activeWildColor);

    /**
     * Chooses a color when playing a WILD or WILD_DRAW_FOUR.
     * @param gameController Provides context.
     * @param hand The player's hand.
     * @return The color chosen for wild use.
     */
    Color chooseWildColor(UnoGame gameController, Hand hand);
}
