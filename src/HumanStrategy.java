import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Strategy implementation for a human-controlled player.
 * Prompts input via console to select cards and wild colors.
 */
public class HumanStrategy implements PlayerStrategy {
    private final Scanner scanner;

    public HumanStrategy(Scanner scanner) {
        this.scanner = Objects.requireNonNull(scanner, "Scanner cannot be null");
    }

    @Override
    public Card chooseCard(UnoGame gameController, Hand hand, List<Card> playableCards, Card topPileCard, Color activeWildColor) {
        if (playableCards.isEmpty()) return null;

        System.out.println("Your playable cards:");
        for (int i = 0; i < playableCards.size(); i++) {
            System.out.println((i + 1) + ": " + playableCards.get(i));
        }

        int choice = -1;
        while (true) {
            System.out.print("Choose card number to play (1-" + playableCards.size() + "): ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            try {
                choice = Integer.parseInt(line);
                if (choice >= 1 && choice <= playableCards.size()) break;
            } catch (NumberFormatException ignored) {}
        }
        return playableCards.get(choice - 1);
    }

    @Override
    public Color chooseWildColor(UnoGame gameController, Hand hand) {
        Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};

        System.out.println("Choose a color for the WILD card:");
        for (int i = 0; i < colors.length; i++) {
            System.out.println((i + 1) + ": " + colors[i]);
        }

        int choice = -1;
        while (true) {
            System.out.print("Choose color number (1-4): ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            try {
                choice = Integer.parseInt(line);
                if (choice >= 1 && choice <= 4) break;
            } catch (NumberFormatException ignored) {}
        }
        return colors[choice - 1];
    }
}
