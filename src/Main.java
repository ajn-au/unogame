import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players (2-4): ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (numPlayers < 2 || numPlayers > 4) {
            System.out.println("Invalid number of players. Exiting.");
            return;
        }

        List<String> playerNames = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            playerNames.add(scanner.nextLine());
        }
        new Game(playerNames).run();
    }
}