package unogame;

import java.util.List;
import java.util.Objects;

/**
 * Manages the state of an Uno game including player turn order and direction.
 * 
  * Author: Andrew Nell
 * Student ID: 110450836
 * Email ID: NELAY007
 * AI Tools Used: None
 * This is my own work as defined by the University's Academic Integrity Policy.
 */
public class GameStateManager {
    private final List<Player> players;
    private int currentPlayerIndex;
    private boolean playDirectionClockwise;
    private boolean skipNextPlayerTurnFlag;
    private Color activeWildColor;
    private boolean isGameRunning;
    
    /**
     * Creates a new game state manager with the specified players.
     * @param players The players participating in the game
     */
    public GameStateManager(List<Player> players) {
        this.players = Objects.requireNonNull(players, "Players list cannot be null");
        if (players.size() < 2) {
            throw new IllegalArgumentException("At least 2 players are required");
        }
        this.currentPlayerIndex = 0;
        this.playDirectionClockwise = true;
        this.skipNextPlayerTurnFlag = false;
        this.activeWildColor = null;
        this.isGameRunning = true;
    }
    
    /**
     * Gets the current player.
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Gets the next player in turn order.
     * @return The next player
     */
    public Player getNextPlayer() {
        return players.get(getNextPlayerIndex());
    }
    
    /**
     * Gets the total number of players.
     * @return Number of players
     */
    public int getNumberOfPlayers() {
        return players.size();
    }
    
    /**
     * Marks the next player's turn to be skipped.
     */
    public void skipNextPlayerTurn() {
        this.skipNextPlayerTurnFlag = true;
    }
    
    /**
     * Reverses the direction of play.
     */
    public void reversePlayDirection() {
        this.playDirectionClockwise = !this.playDirectionClockwise;
    }
    
    /**
     * Sets the active wild color.
     * @param color The chosen wild color
     */
    public void setActiveWildColor(Color color) {
        this.activeWildColor = color;
    }
    
    /**
     * Gets the active wild color.
     * @return The current active wild color
     */
    public Color getActiveWildColor() {
        return activeWildColor;
    }
    
    /**
     * Advances to the next player's turn, respecting direction and skip flags.
     */
    public void advanceToNextPlayer() {
        if (!isGameRunning) return;
        
        if (skipNextPlayerTurnFlag) {
            skipNextPlayerTurnFlag = false;
            advancePlayerIndex();
        }
        advancePlayerIndex();
    }
    
    /**
     * Advances the player index according to the current direction.
     */
    private void advancePlayerIndex() {
        if (playDirectionClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }
    
    /**
     * Calculates the next player index based on current direction.
     * @return The index of the next player
     */
    private int getNextPlayerIndex() {
        if (playDirectionClockwise) {
            return (currentPlayerIndex + 1) % players.size();
        } else {
            return (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }
    
    /**
     * Checks if the game is still running.
     * @return true if the game is running, false otherwise
     */
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
    /**
     * Ends the game.
     */
    public void endGame() {
        isGameRunning = false;
    }
    
    /**
     * Processes a player's winning condition.
     * @param player The player to check
     * @return true if the player has won, false otherwise
     */
    public boolean checkWinCondition(Player player) {
        if (player.getHand().isEmpty()) {
            endGame();
            return true;
        }
        return false;
    }
    
    /**
     * Gets all players in the game.
     * @return List of players
     */
    public List<Player> getAllPlayers() {
        return players;
    }
    
    /**
     * Checks if next player's turn should be skipped.
     * @return true if next player should be skipped, false otherwise
     */
    public boolean isSkipNextPlayerTurnFlagSet() {
        return skipNextPlayerTurnFlag;
    }
    
    /**
     * Gets the current play direction.
     * @return true if clockwise, false if counter-clockwise
     */
    public boolean isPlayDirectionClockwise() {
        return playDirectionClockwise;
    }
}
