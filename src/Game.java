import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Game {

    private final Deck deck;
    private final Pile pile;
    private final List<Player> players;
    private final Scanner scanner; // For user input

    private int currentPlayerIndex;
    private boolean clockwise = true;
    private Color chosenWildColor = null; // Tracks chosen color for a played WILD
    private boolean gameRunning = true;
    private boolean skipNextPlayer = false; // Flag to handle SKIP/DRAW_TWO effect

    /**
     * Creates a new Uno game with the specified player names.
     * @param playerNames List of names for the players.
     */
    public Game (List<String> playerNames) {
        if (playerNames == null || playerNames.size() < 2) {
            throw new IllegalArgumentException("Must have at least 2 players.");
        }
        deck = new Deck();
        pile = new Pile();
        players = new ArrayList<>();
        for (String name : playerNames) {
            players.add(new Player(name));
        }
        scanner = new Scanner(System.in);
        currentPlayerIndex = 0; // Player 0 starts
    }

    /**
     * Starts and runs the Uno game loop.
     */
    public void run() {
        dealInitialHands();
        flipInitialCard();

        System.out.println("\n--- Game Start ---");

        while (gameRunning) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("\n--------------------");
            System.out.println(currentPlayer.getName() + "'s Turn");

            if (skipNextPlayer) {
                System.out.println(currentPlayer.getName() + "'s turn is skipped!");
                skipNextPlayer = false; // Reset flag
                advancePlayer();
                continue; // Move to the next player's turn immediately
            }

            takeTurn(currentPlayer);

            if (!gameRunning) break; // Check if game ended during the turn

            advancePlayer();
        }

        System.out.println("\n--- Game Over ---");
        scanner.close();
    }

    private void dealInitialHands() {
        System.out.println("Dealing hands...");
        for (Player player : players) {
            try {
                 player.getHand().addCards(deck.drawCards(7));
            } catch (IllegalStateException e) {
                System.err.println("Error dealing initial hands: Not enough cards in deck. " + e.getMessage());
                gameRunning = false; // Can't start game
                return;
            }
        }
    }

    private void flipInitialCard() {
        System.out.println("Flipping initial card...");
        Card topCard = null;
        boolean validStartCard = false;
        while (!validStartCard) {
            ensureDeckNotEmpty(); // Make sure deck isn't empty before drawing
            if (!gameRunning) return; // Exit if reshuffle failed

            topCard = deck.drawCard();
            pile.addCard(topCard);
            System.out.println("Initial card: " + topCard);

            if (topCard.getValue() == Value.WILD_DRAW_FOUR) {
                System.out.println("Starting card is WILD DRAW FOUR. Reshuffling into deck.");
                List<Card> reshuffle = pile.takeCardsForReshuffle(); // Should only be this card
                deck.addCards(reshuffle);
                deck.shuffle();
                // Loop continues to draw another card
            } else {
                validStartCard = true;
                // Handle initial card effects (BEFORE first player's turn)
                 handleInitialCardEffect(topCard);
            }
        }
    }
     // Handle effects of the very first card flipped
    private void handleInitialCardEffect(Card card) {
        switch (card.getValue()) {
            case SKIP:
                System.out.println("First card is SKIP. Player 1's turn is skipped.");
                skipNextPlayer = true; // The first player (index 0) will be skipped on their turn
                break;
            case REVERSE:
                System.out.println("First card is REVERSE. Direction reversed.");
                clockwise = !clockwise;
                // In a 2-player game, reverse acts like skip
                if (players.size() == 2) {
                     skipNextPlayer = true;
                     System.out.println(" (Acts as SKIP in 2-player game)");
                }
                break;
            case DRAW_TWO:
                System.out.println("First card is DRAW TWO. Player 1 draws 2 and is skipped.");
                Player firstPlayer = players.get(0);
                 drawCardsForPlayer(firstPlayer, 2);
                skipNextPlayer = true;
                break;
            case WILD:
                System.out.println("First card is WILD.");
                 // Player 0 chooses the color before their turn starts
                 chosenWildColor = promptForWildColor(players.get(0));
                 System.out.println(players.get(0).getName() + " chose " + chosenWildColor);
                break;
            // WILD_DRAW_FOUR already handled in flipInitialCard loop
            // Number cards have no initial effect
            default:
                break;
        }
    }

    private void takeTurn(Player currentPlayer) {
        displayGameState(currentPlayer);

        Card topCard = pile.getTopCard();
        List<Card> validPlays = currentPlayer.getHand().findValidCards(topCard, chosenWildColor);

        Card cardToPlay = null;

        if (validPlays.isEmpty()) {
            System.out.println("No valid cards to play. Drawing a card...");
            drawCardsForPlayer(currentPlayer, 1);
            // Check if the newly drawn card can be played
             Card drawnCard = currentPlayer.getHand().getCards().get(currentPlayer.getHand().getSize() - 1); // Last card drawn
             System.out.println("Drew: " + drawnCard);
            if (drawnCard.canPlayOn(topCard, chosenWildColor)) {
                System.out.println("The drawn card can be played!");
                // Simple AI/Rule: Play immediately if possible
                cardToPlay = drawnCard;
            } else {
                System.out.println("Drawn card cannot be played. Turn ends.");
                 chosenWildColor = null; // Reset any lingering wild choice from previous turn
                return; // End turn after drawing
            }
        } else {
            // Prompt player to choose a card (implement proper input validation)
             cardToPlay = promptForCardPlay(currentPlayer, validPlays);
        }

         // --- Play the chosen card ---
         if (cardToPlay != null) {
            System.out.println(currentPlayer.getName() + " plays " + cardToPlay);
            currentPlayer.getHand().playCard(cardToPlay); // Remove from hand
            pile.addCard(cardToPlay); // Add to pile
            chosenWildColor = null; // Reset wild color choice unless set below

            handleCardEffect(cardToPlay, currentPlayer);

            if (currentPlayer.getHand().isEmpty()) {
                System.out.println("\n" + currentPlayer.getName() + " wins!");
                gameRunning = false;
            }
         } else {
             // This case should ideally not happen if promptForCardPlay is robust,
             // but handle defensively. Could mean player chose not to play drawn card.
             System.out.println(currentPlayer.getName() + "'s turn ends.");
              chosenWildColor = null;
         }
    }

    // Placeholder for player input - needs robust implementation
    private Card promptForCardPlay(Player player, List<Card> validPlays) {
        System.out.println("Your playable cards:");
        for (int i = 0; i < validPlays.size(); i++) {
            System.out.println((i + 1) + ": " + validPlays.get(i));
        }
         // Option to not play drawn card could be added here if needed
        System.out.print("Choose card number to play (1-" + validPlays.size() + "): ");

        int choice = -1;
        while (choice < 1 || choice > validPlays.size()) {
            try {
                choice = scanner.nextInt();
                if (choice < 1 || choice > validPlays.size()) {
                    System.out.print("Invalid choice. Enter number 1-" + validPlays.size() + ": ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Consume invalid input
            }
        }
        scanner.nextLine(); // Consume newline left-over
        return validPlays.get(choice - 1);
    }

     // Placeholder for player input - needs robust implementation
    private Color promptForWildColor(Player player) {
        System.out.println(player.getName() + ", choose a color for the WILD card:");
        for (int i = 0; i < 4; i++) { // Only standard colors
            System.out.println((i + 1) + ": " + Color.values()[i]);
        }
        System.out.print("Choose color number (1-4): ");
        int choice = -1;
         while (choice < 1 || choice > 4) {
             try {
                 choice = scanner.nextInt();
                 if (choice < 1 || choice > 4) {
                     System.out.print("Invalid choice. Enter number 1-4: ");
                 }
             } catch (InputMismatchException e) {
                 System.out.print("Invalid input. Please enter a number: ");
                 scanner.next(); // Consume invalid input
             }
         }
         scanner.nextLine(); // Consume newline
        return Color.values()[choice - 1]; // RED=0, YELLOW=1, etc.
    }


    private void handleCardEffect(Card playedCard, Player currentPlayer) {
        Value value = playedCard.getValue();
        int nextPlayerIndex;

        switch (value) {
            case DRAW_TWO:
                System.out.println("Next player draws two and is skipped.");
                 nextPlayerIndex = getNextPlayerIndex();
                 drawCardsForPlayer(players.get(nextPlayerIndex), 2);
                 skipNextPlayer = true; // Mark the *next* player to be skipped
                break;
            case SKIP:
                 System.out.println("Next player is skipped.");
                 skipNextPlayer = true; // Mark the *next* player to be skipped
                break;
            case REVERSE:
                System.out.print("Direction reversed. ");
                clockwise = !clockwise;
                 if (players.size() == 2) { // In 2 player game, reverse acts like skip
                     System.out.println("(Acts as SKIP)");
                     skipNextPlayer = true;
                 } else {
                     System.out.println();
                 }
                break;
            case WILD:
                 chosenWildColor = promptForWildColor(currentPlayer);
                 System.out.println(currentPlayer.getName() + " chose " + chosenWildColor);
                break;
            case WILD_DRAW_FOUR:
                 System.out.println("Next player draws four and is skipped.");
                 chosenWildColor = promptForWildColor(currentPlayer);
                 System.out.println(currentPlayer.getName() + " chose " + chosenWildColor);
                 nextPlayerIndex = getNextPlayerIndex();
                 drawCardsForPlayer(players.get(nextPlayerIndex), 4);
                 skipNextPlayer = true; // Mark the *next* player to be skipped
                break;
            default: // Number cards
                break;
        }
    }

    private void advancePlayer() {
         if (clockwise) {
             currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
         } else {
             currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
         }
    }

    private int getNextPlayerIndex() {
         if (clockwise) {
              return (currentPlayerIndex + 1) % players.size();
          } else {
              return (currentPlayerIndex - 1 + players.size()) % players.size();
          }
    }

     private void drawCardsForPlayer(Player player, int numCards) {
         System.out.print(player.getName() + " draws " + numCards + " card(s).");
         for (int i=0; i<numCards; ++i) {
             ensureDeckNotEmpty();
             if (!gameRunning) return; // Stop if reshuffle fails
              try {
                   player.getHand().addCard(deck.drawCard());
              } catch (IllegalStateException e) {
                   // Should be caught by ensureDeckNotEmpty, but defensive check
                   System.err.println("\nError drawing card: " + e.getMessage());
                   gameRunning = false;
                   return;
              }
         }
         System.out.println();
     }

     private void ensureDeckNotEmpty() {
         if (deck.isEmpty()) {
             System.out.println("\nDeck is empty! Reshuffling discard pile...");
             List<Card> cardsToReshuffle = pile.takeCardsForReshuffle();
             if (cardsToReshuffle.isEmpty()) {
                 System.out.println("No cards in discard pile to reshuffle. Game might be stuck!");
                 // Consider ending game in a draw or other state here
                 // For now, we'll let it potentially throw later if draw is attempted
                 // gameRunning = false; // Option to end game
                 return;
             }
             deck.addCards(cardsToReshuffle);
             deck.shuffle();
             System.out.println("Deck reshuffled with " + deck.cardsLeft() + " cards.");
         }
     }

    private void displayGameState(Player currentPlayer) {
        System.out.println("Top Card: " + pile.getTopCard() +
                           (chosenWildColor != null ? " (Chosen Color: " + chosenWildColor + ")" : ""));
        System.out.println(currentPlayer.getName() + "'s " + currentPlayer.getHand());
         // Optional: Display other players' hand sizes
         // for (Player p : players) {
         //     if (p != currentPlayer) {
         //         System.out.println(p.getName() + " has " + p.getHand().getSize() + " cards.");
         //     }
         // }
    }


   }