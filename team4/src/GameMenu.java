import java.util.InputMismatchException;
import java.util.Scanner;

public class GameMenu {
    private Player player;
    private GameSession gameSession;
    private int choice;

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    private final Scanner keyboard = new Scanner(System.in);

    public void start() {
        boolean validateInput = true;

        while (validateInput) {
            System.out.print( YELLOW +
                             "╔══════ Binary Puzzle ═════╗\n" +
                             "║        1. Sign up        ║\n" +
                             "║        2. Exit           ║\n" +
                             "╚══════════════════════════╝\n" + RESET);
            System.out.print(BLUE + "Your choice: " + RESET);

            try {
                choice = keyboard.nextInt();
                keyboard.nextLine();

                if (choice == 1) {
                    handleEnterName();
                    validateInput = false;
                } else if (choice == 2) {
                    System.out.println(BLUE + "Goodbye!" + RESET);
                    return;
                } else {
                    System.out.println(RED + "Invalid input! Please enter a number 1 or 2." + RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "Invalid input! Please enter a number 1 or 2." + RESET);
                keyboard.nextLine();
            }
        }

        handleMainMenu();
    }

    public void handleEnterName() {
        boolean validateInput = true;

        while (validateInput) {
            System.out.print(BLUE + "Enter your name: " + RESET);
            String name = keyboard.nextLine();

            if (name == null || name.trim().isEmpty()) {
                System.out.println(RED + "Invalid name! Name can`t be empty!" + RESET);
            } else if (name.length() < 3 || name.length() > 25) {
                System.out.println(RED + "Invalid name! The length of the name should be between 3 and 25 characters." + RESET);
            } else {
                player = new Player(name);
                System.out.print(BLUE + "Welcome, " + GREEN + player.getName() + BLUE + "\n" + RESET);
                validateInput = false;
            }
        }
    }

    public void handleMainMenu() {
        boolean validInput = true;

        while (validInput) {
            displayMainMenu();

            try {
                System.out.print(BLUE + "Your choice: " + RESET);
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
                        System.out.println(BLUE + "Goodbye!" + RESET);
                        validInput = false;
                        break;
                    default:
                        System.out.println(RED + "Invalid input! Please enter number from 1 to 5." + RESET);
                    }
            } catch (Exception e) {
                System.out.println(RED + "Invalid input! Please enter number from 1 to 5." + RESET);
                keyboard.nextLine();
            }
        }
    }

    public void displayMainMenu() {
        System.out.println(YELLOW + "\n╔══════ Binary Puzzle ═════╗\n" +
                                      "║     1. New Game          ║\n" +
                                      "║     2. Continue Game     ║\n" +
                                      "║     3. LeaderBoard       ║\n" +
                                      "║     4. Rules             ║\n" +
                                      "║     5. Exit              ║\n" +
                                      "╚══════════════════════════╝  " + RESET);
    }

    public void displayRules() {
        System.out.println(CYAN +   "╔════════════════════════ How to Play ══════════════════════════╗\n" +
                                    "║   1. Fill the grid with 0s and 1s.                            ║\n" +
                                    "║   2. No more than two of the same number in a row or column.  ║\n" +
                                    "║   3. Equal numbers of 0s and 1s in each row and column.       ║\n" +
                                    "║   4. Rows and columns must be unique.                         ║\n" +
                                    "╚═══════════════════════════════════════════════════════════════╝"   + RESET);
    }
}
