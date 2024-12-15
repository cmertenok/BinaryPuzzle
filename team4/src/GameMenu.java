import java.sql.*;
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
                    "║        2. Sign in        ║\n" +
                    "║        3. Exit           ║\n" +
                    "╚══════════════════════════╝\n" + RESET);
            System.out.print(BLUE + "Your choice: " + RESET);

            try {
                Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
                choice = keyboard.nextInt();
                keyboard.nextLine();

                if (choice == 1) {
                    handleEnterName(connection, keyboard);
                    validateInput = false;
                }else if (choice == 2) {
                    handleSearchName(connection, keyboard);
                    validateInput = false;
                }else if (choice == 3) {
                    System.out.println(BLUE + "Goodbye!" + RESET);
                    return;
                } else {
                    System.out.println(RED + "Invalid input! Please enter a number from 1 to 3." + RESET);
                }
            } catch (SQLException e) {
                System.out.println(RED + "Connection to DB failed." + RESET);
//                e.printStackTrace();
            } catch (InputMismatchException e) {
                System.out.println(RED + "Invalid input! Please enter a number from 1 to 3." + RESET);
                keyboard.nextLine();
            }
        }
        handleMainMenu();
    }

    public void handleEnterName(Connection connection, Scanner keyboard) throws SQLException {
        boolean validateInput = true;

        while (validateInput) {
            System.out.println(BLUE + "Signing you up.");
            System.out.print(BLUE + "Enter your name: " + RESET);
            String name = keyboard.nextLine();

            if (name == null || name.trim().isEmpty()) {
                System.out.println(RED + "Invalid name! Name can`t be empty!" + RESET);
            } else if (name.length() < 3 || name.length() > 25) {
                System.out.println(RED + "Invalid name! The length of the name should be between 3 and 25 characters." + RESET);
            } else {
                player = new Player(name);
                System.out.print(BLUE + "Welcome, " + GREEN + name + BLUE + "\n" + RESET);
                validateInput = false;

                System.out.print(GREEN + name + BLUE + " enter you country: " + RESET);
                String country = keyboard.nextLine();

                String insertQuery = "INSERT INTO players (player_name, player_country) VALUES ('" + name + "' ,'" + country + "')";
                try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Successfully signed up!");
                    } else {
                        System.out.println("Something went wrong!");
                    }
                }
            }
        }
    }

    public void handleSearchName(Connection connection, Scanner keyboard) throws SQLException {
        System.out.println("List of an existing players: ");
        String selectQuery = "SELECT * FROM players";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int playerId = resultSet.getInt("player_id");
                String name = resultSet.getString("player_name");
                System.out.println(playerId + " " + name);
            }
        }
        System.out.print("Enter your ID: ");
        String id = keyboard.nextLine();
        String selectIdQuery = "SELECT * FROM players WHERE player_id = '" + id + "'";
        try (PreparedStatement statement = connection.prepareStatement(selectIdQuery)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("player_name");
                String country = resultSet.getString("player_country");
                System.out.println("Welcome back, " + name + "! From " + country + "!");
                player = new Player(name);
//                player = equals(player) ? null : new Player(name);
            } else {
                System.out.println("Wrong ID!");
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
                        startNewGame();
                        validInput = false;
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

    public void startNewGame() {
        gameSession = new GameSession();
        System.out.println(player.toString());
        System.out.print(gameSession.getBoard());

        while (!gameSession.isBoardComplete()) {
            boolean playerContinues = player.makeMove(gameSession.getBoard());
            if (!playerContinues) {
                return;
            }

            System.out.println(player.toString());
            System.out.println(gameSession.getBoard());
        }

        if(gameSession.validateAmount() && gameSession.validateSequence() && gameSession.validateUniqueness()) {
            System.out.println(GREEN + "Congratulations!\n" + "You solved the problem in " +
                    RED + player.getMovesAmount() + GREEN + " steps!" + RESET + "\uD83D\uDE00");
            handleMainMenu();
        } else {
            System.out.println(RED + "Unfortunately you lose!" + RESET + "\uD83D\uDE22");
            handleMainMenu();
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
        System.out.println(CYAN + "╔════════════════════════ How to Play ══════════════════════════╗\n" +
                "║   1. Fill the grid with 0s and 1s.                            ║\n" +
                "║   2. No more than two of the same number in a row or column.  ║\n" +
                "║   3. Equal numbers of 0s and 1s in each row and column.       ║\n" +
                "║   4. Rows and columns must be unique.                         ║\n" +
                "╚═══════════════════════════════════════════════════════════════╝"   + RESET);
    }

    public void displayLeaderboard() {
        //TODO display LeaderBoard
    }
}
