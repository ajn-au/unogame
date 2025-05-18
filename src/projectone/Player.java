package projectone;

import java.util.List;
import java.util.Objects;

/**
 * Represents a single player in the Uno game.
 * Each player has a name, a hand of cards, and a strategy to choose cards during gameplay.
 *
 * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tools Used: ChatGPT (documentation and formatting assistance)
 * This is my own work as defined by the University's Academic Integrity Policy.
 */
public class Player {
    private final String name;
    private final Hand hand;
    private final PlayerStrategy strategy;

    /**
     * Constructs a new COMP2033_Project_1_110450836.Player with a name and strategy.
     * @param name The player's name. Must not be null or blank.
     * @param strategy The strategy used by the player. Must not be null.
     */
    public Player(String name, PlayerStrategy strategy) {
        Objects.requireNonNull(name, "COMP2033_Project_1_110450836.Player name cannot be null");
        Objects.requireNonNull(strategy, "COMP2033_Project_1_110450836.Player strategy cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("COMP2033_Project_1_110450836.Player name cannot be blank");
        }
        this.name = name;
        this.hand = new Hand();
        this.strategy = strategy;
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    /**
     * Uses the strategy to choose a card to play from the list of valid options.
     * @param gameController Game context to assist strategy decisions.
     * @param playableCards List of valid cards this turn.
     * @return Chosen card or null if none selected.
     */
    public Card chooseCardToPlay(UnoGame gameController, List<Card> playableCards) {
        Objects.requireNonNull(gameController);
        Objects.requireNonNull(playableCards);
        return strategy.chooseCard(gameController, hand, playableCards, gameController.getPile().getTopCard(), gameController.getActiveWildColor());
    }

    /**
     * Uses the strategy to choose a color for a WILD card.
     * @param gameController The game context.
     * @return Chosen color.
     */
    public Color chooseWildColor(UnoGame gameController) {
        Objects.requireNonNull(gameController);
        return strategy.chooseWildColor(gameController, hand);
    }

    @Override
    public String toString() {
        return "COMP2033_Project_1_110450836.Player: " + name + " (COMP2033_Project_1_110450836.Hand: " + hand.getSize() + " cards)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
