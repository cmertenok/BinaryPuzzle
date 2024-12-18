import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                try {
                    Statement statement = connection.createStatement();
                    int rowsInserted = statement.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
                    if (rowsInserted > 0) {
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int newPlayerId = generatedKeys.getInt(1);
                            player.setPlayerID(newPlayerId);
                            System.out.println(GREEN + "Successfully signed up! Your player ID is: " + newPlayerId + RESET);
                        }
                    } else {
                        System.out.println(RED + "Something went wrong!" + RESET);
                    }

                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
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
                System.out.printf("%-3s %5s%n", playerId, name);
            }
        }

        System.out.print("Enter your ID: ");
        String id = keyboard.nextLine();
        String selectIdQuery = "SELECT * FROM players WHERE player_id = '" + id + "'";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectIdQuery);
            if (resultSet.next()) {
                String name = resultSet.getString("player_name");
                String country = resultSet.getString("player_country");
                System.out.println("Welcome back, " + name + "! From " + country + "!");
                player = new Player(name);
                player.setPlayerID(Integer.parseInt(id));
            } else {
                System.out.println("Wrong ID!");
            }
        }
    }

    public void handleMainMenu() {
        boolean validInput = true;

        while (validInput) {
            displayMainMenu();

            System.out.print(BLUE + "Your choice: " + RESET);
            String input = keyboard.nextLine().trim();

            try {
                choice = Integer.parseInt(input);
                switch (choice) {
                    case 1:
                        startGame(null);
                        validInput = false;
                        break;
                    case 2:
                        Board board = new Board();
                        board.loadBoard();
                        startGame(board);
                        validInput = false;
                        break;
                    case 3:
                        displayLeaderboard();
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
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input! Please enter number from 1 to 5." + RESET);
            }
        }
    }

    public void startGame(Board board) {
        gameSession = new GameSession();
        player.setGameSession(gameSession);

        if (board == null) {
            initializeGame();
        } else {
            int loadedGameID = getSavedGameID();
            try {
                Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT score FROM game_info WHERE game_id = " + loadedGameID + ";");
                if (resultSet.next()) {
                    player.setMoves(resultSet.getInt("score"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (loadedGameID == -1) {
                System.out.println(RED + "You don't have any saves!" + RESET);
                return;
            }

            gameSession.setGameID(loadedGameID);
            gameSession.setBoard(board);
        }

        System.out.println(player.toString());
        System.out.print(gameSession.getBoard());

        while (!gameSession.isBoardComplete()) {
            boolean playerContinues = player.makeMove(gameSession.getBoard());
            if (!playerContinues) {
                return;
            }

            try (Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
                 Statement statement = connection.createStatement()) {

                String updateScoreQuery = "UPDATE game_info SET score = " + player.getMovesAmount() +
                        " WHERE game_id = " + gameSession.getGameID();
                statement.executeUpdate(updateScoreQuery);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.println(player.toString());
            System.out.println(gameSession.getBoard());
        }

        try (Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
             Statement statement = connection.createStatement()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String endDate = LocalDateTime.now().format(formatter);

            if (gameSession.validateAmount() && gameSession.validateSequence() && gameSession.validateUniqueness()) {
                String updateStatusQuery = "UPDATE game_info SET game_outcome = 'Win', end_time = '" + endDate + "' WHERE game_id = " + gameSession.getGameID();
                statement.executeUpdate(updateStatusQuery);

                gameSession.setEndTime(LocalDateTime.now());
                System.out.println(GREEN + "Congratulations!\n" + "You solved the problem in " +
                        RED + player.getMovesAmount() + GREEN + " steps!" + RESET + "\uD83D\uDE00");
            } else {
                String updateStatusQuery = "UPDATE game_info SET game_outcome = 'Lose', end_time = '" + endDate + "' WHERE game_id = " + gameSession.getGameID();
                statement.executeUpdate(updateStatusQuery);

                gameSession.setEndTime(LocalDateTime.now());
                System.out.println(RED + "Unfortunately you lose!" + RESET + "\uD83D\uDE22");
            }

            player.setMoves(0);
            handleMainMenu();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSavedGameID() {
        int id = -1;
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT game_id FROM board_state FETCH FIRST ROW ONLY");
            if (resultSet.next()) {
                id = resultSet.getInt("game_id");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    public void initializeGame() {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
            Statement statement = connection.createStatement();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String startDate = LocalDateTime.now().format(formatter);
            gameSession.setStartTime(LocalDateTime.now());

            String insertGameQuery = "INSERT INTO game_info (player_id, start_time, score, session_duration) " +
                    "VALUES (" + player.getPlayerID() + ", '" + startDate + "', 0, 0)";

            statement.executeUpdate(insertGameQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int gameId = generatedKeys.getInt(1);
                gameSession.setGameID(gameId);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        Scanner scanner = new Scanner(System.in);

        System.out.println(YELLOW + "══════ LeaderBoard ═════" + RESET);
        String showTop = "SELECT p.player_name, g.score\n" +
                "FROM players p\n" +
                "JOIN game_info g ON p.player_id = g.player_id\n" +
                "WHERE UPPER(g.game_outcome) = 'WIN'\n" +
                "ORDER BY g.score ASC\n" +
                "FETCH FIRST 5 ROWS ONLY;";

        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(showTop);
            int place = 1;

            while (resultSet.next()) {
                String name = resultSet.getString("player_name");
                int score = resultSet.getInt("score");

                String formattedTop = String.format("%3d. ", place);
                String formattedName = String.format("%-10s", name);
                String formattedScore = String.format("%5d", score);

                System.out.println(YELLOW + formattedTop + GREEN + formattedName + RESET + " " + RED + formattedScore + RESET);
                place++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            System.out.println(YELLOW + "Enter name to see more, or type " + RED + "'menu' " + YELLOW + "to return to menu: " + RESET);
            String input = scanner.nextLine().trim();

            if (input.toLowerCase().equals("menu")) {
                handleMainMenu();
                return;
            }

            String showPlayerStats = "SELECT g.score\n" +
                    "FROM players p\n" +
                    "JOIN game_info g on p.player_id = g.player_id\n" +
                    "WHERE LOWER(p.player_name) ='" + input.toLowerCase() + "' AND UPPER(g.game_outcome) = 'WIN' \n" +
                    "ORDER BY g.score ASC\n" +
                    "FETCH FIRST 5 ROWS ONLY;";

            try {
                Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(showPlayerStats);

                boolean hasResults = false;
                int place = 1;

                while (resultSet.next()) {
                    hasResults = true;
                    int score = resultSet.getInt("score");

                    String formattedTop = String.format("%3d. ", place);
                    String formattedScore = String.format("%5d", score);

                    System.out.println(YELLOW + formattedTop + RED + formattedScore + RESET);
                    place++;
                }

                if (!hasResults) {
                    System.out.println(RED + "Player not found!" + RESET);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
