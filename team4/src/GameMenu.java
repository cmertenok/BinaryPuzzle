import java.util.Scanner;

public class GameMenu {
    private int choice;
    private final Scanner scanner = new Scanner(System.in);

    public int getChoice() {
        return choice;
    }

    public void displayMenu() {
        System.out.println("=== Binary Puzzle Game Menu ===");
        System.out.println("1. Start New Game");
        System.out.println("2. Continue Game");
        System.out.println("3. LeaderBoard");
        System.out.println("4. View Rules");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    public void displayRules() {
        System.out.println("\n===== Game Rules =====");
        System.out.println("1. Fill the grid with 0s and 1s.");
        System.out.println("2. No more than two of the same number in a row or column.");
        System.out.println("3. Equal numbers of 0s and 1s in each row and column.");
        System.out.println("4. Rows and columns must be unique.\n");
    }

    public void handleMenu() {
        displayMenu();
        choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Starting a new game...");
                break;
            case 2:
                System.out.println("Continuing the game...");
                break;
            case 3:
                System.out.println("Displaying Leaderboard...");
                break;
            case 4:
                displayRules();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Exiting.");
                return;
        }

        System.out.println("Press 'x' to go back to the menu or any other key to exit.");
        String backChoice = scanner.next();

        if (backChoice.equalsIgnoreCase("x")) {
            handleMenu();
        } else {
            System.out.println("Exiting the game.");
        }
        scanner.close();
    }
}
