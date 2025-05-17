package COMP2033_Project_1_110450836;

import java.util.*;

public class UnoGame {
    private final Deck deck;
    private final Pile pile;
    private final List<Player> players;
    private final Scanner humanInteractionScannerInstance;

    private int currentPlayerIndex;
    private boolean playDirectionClockwise = true;
    private Color activeWildColor = null;
    private boolean isGameRunning = true;
    private boolean skipNextPlayerTurnFlag = false;

    public UnoGame(List<PlayerStrategy> playerStrategies, List<String> playerNames, long deckSeed, Scanner scannerForHumanSetup) {
        validateInputs(playerStrategies, playerNames);
        this.deck = new Deck(deckSeed);
        this.pile = new Pile();
        this.players = new ArrayList<>();

        for (int i = 0; i < playerNames.size(); i++) {
            players.add(new Player(playerNames.get(i), playerStrategies.get(i)));
        }
        this.humanInteractionScannerInstance = scannerForHumanSetup;
        this.currentPlayerIndex = 0;
    }

    private void validateInputs(List<PlayerStrategy> playerStrategies, List<String> playerNames) {
        Objects.requireNonNull(playerNames);
        Objects.requireNonNull(playerStrategies);
        if (playerNames.size() < 2 || playerStrategies.size() != playerNames.size()) {
            throw new IllegalArgumentException("Must have at least 2 players with matching strategies and names.");
        }
    }

    public void run() {
        try {
            startGame();
        } finally {
            closeScannerIfNeeded();
        }
    }

    private void startGame() {
        if (!dealInitialHands() || !flipInitialCard()) return;

        System.out.println("\n--- Uno Game Start ---");
        while (isGameRunning) {
            handlePlayerTurn();
        }
        System.out.println("\n--- Game Over ---");
    }

    private void handlePlayerTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println("\n====================");
        System.out.println(currentPlayer.getName() + "'s Turn");

        if (skipNextPlayerTurnFlag) {
            System.out.println(currentPlayer.getName() + "'s turn is SKIPPED!");
            skipNextPlayerTurnFlag = false;
            advancePlayer();
            return;
        }

        playTurn(currentPlayer);
        if (isGameRunning) advancePlayer();
    }

    private boolean dealInitialHands() {
        System.out.println("Dealing initial 7 cards to each player...");
        for (Player player : players) {
            try {
                player.getHand().addCards(deck.drawCards(7));
            } catch (IllegalStateException e) {
                System.err.println("CRITICAL ERROR: Not enough cards in deck for initial deal. " + e.getMessage());
                isGameRunning = false;
                return false;
            }
        }
        System.out.println("Initial hands dealt.");
        return true;
    }

    private boolean flipInitialCard() {
        System.out.println("Flipping initial card onto the pile...");
        while (true) {
            if (!reshufflePile()) {
                System.err.println("CRITICAL ERROR: COMP2033_Project_1_110450836.Deck and pile empty. Cannot flip starting card.");
                isGameRunning = false;
                return false;
            }
            Card firstCard = deck.drawCard();
            System.out.println("Flipped: " + firstCard);
            if (firstCard.getValue() == Value.WILD_DRAW_FOUR) {
                System.out.println("WILD_DRAW_FOUR cannot be starting card. Reshuffling.");
                deck.addCards(Collections.singletonList(firstCard));
                deck.shuffle();
                continue;
            }
            pile.addCard(firstCard);
            firstCard.applyEffect(this);
            if (firstCard.getValue() == Value.WILD) {
                Player firstPlayer = players.get(0);
                Color chosen = firstPlayer.chooseWildColor(this);
                setActiveWildColor(chosen);
                System.out.println(firstPlayer.getName() + " chose starting color: " + chosen);
            }
            break;
        }
        return true;
    }

    private void playTurn(Player player) {
        displayGameState(player);
        List<Card> playable = player.getHand().findValidCards(pile.getTopCard(), activeWildColor);
        Card cardToPlay = playable.isEmpty() ? handleCardEffect(player) : player.chooseCardToPlay(this, playable);

        if (cardToPlay != null && isCardPlayable(cardToPlay, player)) {
            playCard(player, cardToPlay);
        } else {
            System.out.println(player.getName() + " cannot play. Turn passes.");
        }
    }

    private Card handleCardEffect(Player player) {
        System.out.println(player.getName() + " has no valid cards. Drawing a card...");
        if (!reshufflePile()) return null;
        Card drawn = deck.drawCard();
        System.out.println(player.getName() + " drew: " + drawn);
        player.getHand().addCard(drawn);
        return drawn.canPlayOn(pile.getTopCard(), activeWildColor) ? drawn : null;
    }

    private boolean isCardPlayable(Card card, Player player) {
        return player.getHand().getCards().contains(card) &&
               card.canPlayOn(pile.getTopCard(), activeWildColor);
    }

    private void playCard(Player player, Card card) {
        System.out.println(player.getName() + " plays " + card);
        player.getHand().playCard(card);
        pile.addCard(card);
        activeWildColor = null;
        card.applyEffect(this);

        if (player.getHand().isEmpty()) {
            System.out.println("\n" + player.getName() + " exclaims 'UNO!' and WINS!");
            isGameRunning = false;
        }
    }

    private void advancePlayer() {
        int size = players.size();
        currentPlayerIndex = playDirectionClockwise
            ? (currentPlayerIndex + 1) % size
            : (currentPlayerIndex - 1 + size) % size;
    }

    private boolean reshufflePile() {
        if (!deck.isEmpty()) return true;
        List<Card> reshuffle = pile.takeCardsForNewDeck();
        if (reshuffle.isEmpty()) return false;
        System.out.println("\nCOMP2033_Project_1_110450836.Deck is empty! Reshuffling discard pile...");
        deck.addCards(reshuffle);
        deck.shuffle();
        System.out.println("COMP2033_Project_1_110450836.Deck reshuffled with " + deck.cardsRemaining() + " cards.");
        return true;
    }

    private void displayGameState(Player player) {
        System.out.println("COMP2033_Project_1_110450836.Pile Top: " + pile.getTopCard() +
                (activeWildColor != null ? " (Active Wild COMP2033_Project_1_110450836.Color: " + activeWildColor + ")" : ""));
        for (Player p : players) System.out.print(p.getName() + ":" + p.getHand().getSize() + " | ");
        System.out.println("\n" + player.getName() + "'s COMP2033_Project_1_110450836.Hand: " + player.getHand());
    }

    private void closeScannerIfNeeded() {
        if (humanInteractionScannerInstance != null) {
            humanInteractionScannerInstance.close();
        }
    }

    public void skipNextPlayerTurn() { skipNextPlayerTurnFlag = true; }
    public void reversePlayDirection() { playDirectionClockwise = !playDirectionClockwise; }
    public void setActiveWildColor(Color color) { activeWildColor = color; }
    public void makePlayerDraw(Player player, int count) {
        for (int i = 0; i < count; i++) {
            if (!reshufflePile()) break;
            player.getHand().addCard(deck.drawCard());
        }
        System.out.println(player.getName() + " drew " + count + " card(s). COMP2033_Project_1_110450836.Hand size now " + player.getHand().getSize());
    }
    public int getNumberOfPlayers() { return players.size(); }
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public Player getNextPlayer() {
        int size = players.size();
        int nextIndex = playDirectionClockwise
            ? (currentPlayerIndex + 1) % size
            : (currentPlayerIndex - 1 + size) % size;
        return players.get(nextIndex);
    }
    public Pile getPile() { return pile; }
    public Color getActiveWildColor() { return activeWildColor; }
    public Scanner getHumanInteractionScanner() { return humanInteractionScannerInstance; }
    
    public static void main(String[] args) {
        Scanner setupScanner = new Scanner(System.in);
        List<String> playerNames = new ArrayList<>();
        List<PlayerStrategy> playerStrategies = new ArrayList<>();

        int numPlayers = 0;
        while (numPlayers < 2 || numPlayers > 4) {
            System.out.print("Enter number of players (2-4): ");
            try {
                String line = setupScanner.nextLine().trim();
                if (line.isEmpty()){ System.out.println("Please enter a number."); continue;}
                numPlayers = Integer.parseInt(line);
                if (numPlayers < 2 || numPlayers > 4) {
                    System.out.println("Invalid number. Must be between 2 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        for (int i = 0; i < numPlayers; i++) {
            String name;
            while(true){
                System.out.print("Enter name for COMP2033_Project_1_110450836.Player " + (i + 1) + ": ");
                name = setupScanner.nextLine().trim();
                if(!name.isEmpty()) break;
                System.out.println("COMP2033_Project_1_110450836.Player name cannot be empty.");
            }
            playerNames.add(name);
            if (i == 0) {
                playerStrategies.add(new HumanStrategy(setupScanner));
            } else {
                playerStrategies.add(new BasicAIStrategy());
            }
        }

        System.out.print("Enter a seed for deck shuffle (leave blank for random time-based seed): ");
        long seed;
        String seedInput = setupScanner.nextLine().trim();
        try {
            if (seedInput.isEmpty()) {
                seed = System.currentTimeMillis();
                System.out.println("Using random seed: " + seed);
            } else {
                seed = Long.parseLong(seedInput);
                System.out.println("Using provided seed: " + seed);
            }
        } catch (NumberFormatException e) {
            seed = System.currentTimeMillis();
            System.out.println("Invalid seed input. Using random seed: " + seed);
        }

        UnoGame unoGame = new UnoGame(playerStrategies, playerNames, seed, setupScanner);
        unoGame.run();
        // The setupScanner is passed to COMP2033_Project_1_110450836.HumanStrategy instances, and Game will close it if it's not System.in
    }

    
}
