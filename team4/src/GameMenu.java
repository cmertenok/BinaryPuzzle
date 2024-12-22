import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            System.out.print( Color.YELLOW +
                    "╔══════ Binary Puzzle ═════╗\n" +
                    "║        1. Sign up        ║\n" +
                    "║        2. Sign in        ║\n" +
                    "║        3. Exit           ║\n" +
                    "╚══════════════════════════╝\n" + Color.RESET);
            System.out.print(Color.BLUE + "Your choice: " + Color.RESET);

            try {
                choice = keyboard.nextInt();
                keyboard.nextLine();

                if (choice == 1) {
                    handleEnterName();
                    validateInput = false;
                }else if (choice == 2) {
                    handleSearchName();
                    validateInput = false;
                }else if (choice == 3) {
                    System.out.println(Color.BLUE + "Goodbye!" + Color.RESET);
                    return;
                } else {
                    System.out.println(Color.RED + "Invalid input! Please enter a number from 1 to 3." + Color.RESET);
                }

            } catch (SQLException e) {
                System.out.println(Color.RED + "Connection to DB failed." + Color.RESET);
            } catch (InputMismatchException e) {
                System.out.println(Color.RED + "Invalid input! Please enter a number from 1 to 3." + Color.RESET);
                keyboard.nextLine();
            }
        }
        handleMainMenu();
    }

    public void handleEnterName() throws SQLException {
        boolean validateInput = true;

        while (validateInput) {
            System.out.print(Color.BLUE + "Enter your name: " + Color.RESET);
            String name = keyboard.nextLine();

            if (name == null || name.trim().isEmpty()) {
                System.out.println(Color.RED + "Invalid name! Name can`t be empty!" + Color.RESET);
            } else if (name.length() < 3 || name.length() > 25) {
                System.out.println(Color.RED + "Invalid name! The length of the name should be between 3 and 25 characters." + Color.RESET);
            } else {
                player = new Player(name);
                System.out.print(Color.BLUE + "Welcome, " + Color.GREEN + name + Color.BLUE + "\n" + Color.RESET);
                validateInput = false;

                System.out.print(Color.GREEN + name + Color.BLUE + " enter you country: " + Color.RESET);
                String country = keyboard.nextLine();

                String insertQuery = "INSERT INTO players (player_name, player_country) VALUES ('" + name + "' ,'" + country + "')";
                try {
                    Connection connection = DatabaseConnection.getConnection();
                    Statement statement = connection.createStatement();
                    int rowsInserted = statement.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
                    if (rowsInserted > 0) {
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int newPlayerId = generatedKeys.getInt(1);
                            player.setPlayerID(newPlayerId);
                            System.out.println(Color.GREEN + "Successfully signed up! Your player ID is: " + newPlayerId + Color.RESET);
                        }
                    } else {
                        System.out.println(Color.RED + "Something went wrong!" + Color.RESET);
                    }

                    statement.close(); connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void handleSearchName() throws SQLException {
        System.out.println(Color.BLUE + "List of an existing players: " + Color.RESET);
        String selectQuery = "SELECT * FROM players";
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                String playerId = resultSet.getInt("player_id") + "";
                String name = resultSet.getString("player_name");
                System.out.printf("%-3s %5s%n", Color.RED + playerId, Color.YELLOW + name + Color.RESET);
            }
            statement.close(); connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            System.out.print(Color.BLUE + "Enter your ID: " + Color.RESET);
            String id = keyboard.nextLine();
            String selectIdQuery = "SELECT * FROM players WHERE player_id = '" + id + "'";

            try (Connection connection = DatabaseConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectIdQuery)) {

                if (resultSet.next()) {
                    String name = resultSet.getString("player_name");
                    String country = resultSet.getString("player_country");
                    System.out.println(Color.BLUE + "Welcome back, " + Color.YELLOW + name +
                                       Color.BLUE + " from " + Color.YELLOW + country + "!");
                    player = new Player(name);
                    player.setPlayerID(Integer.parseInt(id));
                    break;
                } else {
                    System.out.println(Color.RED + "Wrong ID! Please try again." + Color.RESET);
                }
            } catch (SQLException e) {
                System.out.println(Color.RED + "Wrong ID! Please try again." + Color.RESET);
            }
        }
    }

    public void handleMainMenu() {
        boolean validInput = true;

        while (validInput) {
            displayMainMenu();

            System.out.print(Color.BLUE + "Your choice: " + Color.RESET);
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
                        System.out.println(Color.BLUE + "Goodbye!" + Color.RESET);
                        validInput = false;
                        break;
                    default:
                        System.out.println(Color.RED + "Invalid input! Please enter number from 1 to 5." + Color.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Invalid input! Please enter number from 1 to 5." + Color.RESET);
            }
        }
    }

    public void startGame(Board board) {
        gameSession = new GameSession();
        player.setGameSession(gameSession);

        if (board == null) {
            gameSession.setStartTime(LocalDateTime.now());
            initializeGame();
        } else {
            int loadedGameID = getSavedGameID();
            try {
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT score FROM game_info WHERE game_id = " + loadedGameID + ";");
                if (resultSet.next()) {
                    player.setMoves(resultSet.getInt("score"));
                    gameSession.setStartTime(LocalDateTime.now());
                }

                statement.close(); connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (loadedGameID == -1) {
                System.out.println(Color.RED + "You don't have any saves!" + Color.RESET);
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
                gameSession.updateSessionDuration();
                return;
            }

            String updateScoreQuery = "UPDATE game_info SET score = " + player.getMovesAmount() +
                                      " WHERE game_id = " + gameSession.getGameID();
            try {
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(updateScoreQuery);
                statement.close(); connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.println(player.toString());
            System.out.println(gameSession.getBoard());
        }

        gameSession.setEndTime(LocalDateTime.now());
        gameSession.updateSessionDuration();

        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String endDate = LocalDateTime.now().format(formatter);

            if (gameSession.validateAmount() && gameSession.validateSequence() && gameSession.validateUniqueness()) {
                String updateStatusQuery = "UPDATE game_info SET game_outcome = 'Win', end_time = '" +
                                            endDate + "' WHERE game_id = " + gameSession.getGameID();
                statement.executeUpdate(updateStatusQuery);

                gameSession.setEndTime(LocalDateTime.now());
                System.out.println(Color.GREEN + "Congratulations!\n" + "You solved the problem in " +
                        Color.RED + player.getMovesAmount() + Color.GREEN + " steps!" + Color.RESET + "\uD83D\uDE00");
            } else {
                String updateStatusQuery = "UPDATE game_info SET game_outcome = 'Lose', end_time = '" +
                                            endDate + "' WHERE game_id = " + gameSession.getGameID();
                statement.executeUpdate(updateStatusQuery);

                gameSession.setEndTime(LocalDateTime.now());
                System.out.println(Color.RED + "Unfortunately you lose!" + Color.RESET + "\uD83D\uDE22");
            }

            player.setMoves(0);
            handleMainMenu();

        statement.close(); connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSavedGameID() {
        int id = -1;
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT game_id FROM board_state FETCH FIRST ROW ONLY");
            if (resultSet.next()) {
                id = resultSet.getInt("game_id");
            }

        statement.close(); connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    public void initializeGame() {
        try {
            Connection connection = DatabaseConnection.getConnection();
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

        statement.close(); connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayMainMenu() {
        System.out.println(Color.YELLOW + "\n╔══════ Binary Puzzle ═════╗\n" +
                                            "║     1. New Game          ║\n" +
                                            "║     2. Continue Game     ║\n" +
                                            "║     3. LeaderBoard       ║\n" +
                                            "║     4. Rules             ║\n" +
                                            "║     5. Exit              ║\n" +
                                            "╚══════════════════════════╝  " + Color.RESET);
    }

    public void displayRules() {
        System.out.println(Color.CYAN + "╔════════════════════════ How to Play ══════════════════════════╗\n" +
                                        "║   1. Fill the grid with 0s and 1s.                            ║\n" +
                                        "║   2. No more than two of the same number in a row or column.  ║\n" +
                                        "║   3. Equal numbers of 0s and 1s in each row and column.       ║\n" +
                                        "║   4. Rows and columns must be unique.                         ║\n" +
                                        "╚═══════════════════════════════════════════════════════════════╝"   + Color.RESET);
    }

    public void displayLeaderboard() {

        System.out.println(Color.YELLOW + "══════ LeaderBoard ═════" + Color.RESET);
        String showTop = "SELECT p.player_name, g.score\n" +
                         "FROM players p\n" +
                         "JOIN game_info g ON p.player_id = g.player_id\n" +
                         "WHERE UPPER(g.game_outcome) = 'WIN'\n" +
                         "ORDER BY g.score ASC\n" +
                         "FETCH FIRST 5 ROWS ONLY;";
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(showTop);
            int place = 1;

            while (resultSet.next()) {
                String name = resultSet.getString("player_name");
                int score = resultSet.getInt("score");

                String formattedTop = String.format("%3d. ", place);
                String formattedName = String.format("%-10s", name);
                String formattedScore = String.format("%5d", score);

                System.out.println(Color.YELLOW + formattedTop + Color.GREEN + formattedName +
                                   Color.RESET + " " + Color.RED + formattedScore + Color.RESET);
                place++;
            }

        statement.close(); connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            System.out.println(Color.YELLOW + "Enter name to see more, or type " + Color.RED + "'menu' " +
                               Color.YELLOW + "to return to menu: " + Color.RESET);
            String input = keyboard.nextLine().trim();

            if (input.toLowerCase().equals("menu")) {
                handleMainMenu();
                return;
            }

            String showPlayerStats = "SELECT g.score\n" +
                                     "FROM players p\n" +
                                     "JOIN game_info g on p.player_id = g.player_id\n" +
                                     "WHERE LOWER(p.player_name) ='" + input.toLowerCase() +
                                     "' AND UPPER(g.game_outcome) = 'WIN' \n" +
                                     "ORDER BY g.score ASC\n" +
                                     "FETCH FIRST 5 ROWS ONLY;";
            try {
                Connection connection = DatabaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(showPlayerStats);

                boolean hasResults = false;
                int place = 1;

                while (resultSet.next()) {
                    hasResults = true;
                    int score = resultSet.getInt("score");

                    String formattedTop = String.format("%3d. ", place);
                    String formattedScore = String.format("%5d", score);

                    System.out.println(Color.YELLOW + formattedTop + Color.RED + formattedScore + Color.RESET);
                    place++;
                }

                if (!hasResults) {
                    System.out.println(Color.RED + "Player not found!" + Color.RESET);
                }

            statement.close(); connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
