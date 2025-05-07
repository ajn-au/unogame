/**
 * Represents a player in the Uno game, holding a name and a hand of cards.
 *
 * Author: [Your Name]
 * Student ID: [Your ID]
 * Email ID: [Your Email ID]
 * AI Tool Used: [Specify if used]
 * This is my own work as defined by the University's Academic Integrity Policy.
 */
public class Player {
    private final String name;
    private final Hand hand;

    /**
     * Creates a new player with the given name and an empty hand.
     * @param name The player's name.
     */
    public Player(String name) {
        if (name == null || name.trim().isEmpty()) {
             throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        this.name = name;
        this.hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return "Player: " + name;
    }

    // equals/hashCode based on name might be useful if comparing Player objects directly
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}