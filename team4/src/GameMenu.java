import java.util.InputMismatchException;
import java.util.Scanner;

public class GameMenu {
    private Player player;
    private GameSession gameSession;
    private int choice;

    private final Scanner keyboard = new Scanner(System.in);

    public void start() {
        boolean validateInput = true;

        while (validateInput) {
            System.out.println("╔══════ Binary Puzzle ═════╗");
            System.out.println("║        1. Sign up        ║");
            System.out.println("║        2. Exit           ║");
            System.out.print  ("╚══════════════════════════╝\n");
            System.out.print("Your choice: ");

            try {
                choice = keyboard.nextInt();
                keyboard.nextLine();

                if (choice == 1) {
                    handleEnterName();
                    validateInput = false;
                } else if (choice == 2) {
                    System.out.println("Goodbye!");
                    return;
                } else {
                    System.out.println("Invalid input! Please enter a number 1 or 2.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number 1 or 2.");
                keyboard.nextLine();
            }
        }

        handleMainMenu();
    }

    public void handleEnterName() {
        boolean validateInput = true;

        while (validateInput) {
            System.out.print("Enter your name: ");
            String name = keyboard.nextLine();

            if (name == null || name.trim().isEmpty()) {
                System.out.println("Username can't be empty!");
            } else if (name.length() < 3) {
                System.out.println("Username can't be less than 3 characters!");
            } else {
                player = new Player(name);
                System.out.printf("\nWelcome, %s!", player.getName());
                validateInput = false;
            }
        }
    }

    public void handleMainMenu() {
        boolean validInput = true;

        while (validInput) {
            displayMainMenu();

            try {
                System.out.print("Your choice: ");
                choice = keyboard.nextInt();
                keyboard.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Starting a new game...");
                        // TODO Staring new game
                        break;
                    case 2:
                        System.out.println("Continuing the game...");
                        // TODO Loading game
                        break;
                    case 3:
                        System.out.println("Displaying Leaderboard...");
                        // TODO Displaying Leaderboard
                        break;
                    case 4:
                        displayRules();
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        validInput = false;
                        break;
                    default:
                        System.out.println("Invalid input! Please enter number from 1 to 5.");
                        keyboard.nextLine();
                    }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter number from 1 to 5.");
                keyboard.nextLine();
            }
        }
    }

    public void displayMainMenu() {
        System.out.println();
        System.out.println("╔══════ Binary Puzzle ═════╗");
        System.out.println("║     1. New Game          ║");
        System.out.println("║     2. Continue Game     ║");
        System.out.println("║     3. LeaderBoard       ║");
        System.out.println("║     4. Rules             ║");
        System.out.println("║     5. Exit              ║");
        System.out.print  ("╚══════════════════════════╝\n");
    }

    public void displayRules() {
        System.out.println("╔════════════════════════ How to Play ══════════════════════════╗");
        System.out.println("║   1. Fill the grid with 0s and 1s.                            ║");
        System.out.println("║   2. No more than two of the same number in a row or column.  ║");
        System.out.println("║   3. Equal numbers of 0s and 1s in each row and column.       ║");
        System.out.println("║   4. Rows and columns must be unique.                         ║");
        System.out.print  ("╚═══════════════════════════════════════════════════════════════╝\n");
    }
}
