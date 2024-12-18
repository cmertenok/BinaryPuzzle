import java.sql.*;
import java.util.Random;

public class Board {
    public static final int BOARD_SIZE = 4;
    public Cell[][] gameBoard;

    private final Random rand = new Random();

    public Board() {
        this.gameBoard = new Cell[BOARD_SIZE + 1][BOARD_SIZE + 1];
        for (int i = 0; i < BOARD_SIZE + 1; i++) {
            for (int j = 0; j < BOARD_SIZE + 1; j++) {
                gameBoard[i][j] = new Cell();
            }
        }

        for (int i = 0; i < 5; i++) {
            generateStartValues();
        }
    }

    public Cell getCellValue(int row, int col) {
        return gameBoard[row][col];
    }

    public void generateStartValues() {
        String value = GameMenu.RED + rand.nextInt(2) + GameMenu.RESET;
        int randomRow, randomCol;

        do {
            randomRow = rand.nextInt(BOARD_SIZE) + 1;
            randomCol = rand.nextInt(BOARD_SIZE) + 1;
        } while (!gameBoard[randomRow][randomCol].isEmpty());

        gameBoard[randomRow][randomCol].setValue(value);
        gameBoard[randomRow][randomCol].setLocked(true);
    }

    public boolean validateMove(int row, int col, String value) {
        if (row < 1 || row > BOARD_SIZE || col < 1 || col > BOARD_SIZE) {
            return false;
        } else if (gameBoard[row][col].isLocked()) {
            return false;
        } else {
            gameBoard[row][col].setValue(value);
            return true;
        }
    }

    public void saveBoard(int gameID) {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
            Statement statement = connection.createStatement();
            String deleteQuery = "DELETE FROM board_state;";
            statement.executeUpdate(deleteQuery);

            for (int i = 1; i <= BOARD_SIZE; i++) {
                for (int j = 1; j <= BOARD_SIZE; j++) {
                    Cell cell = gameBoard[i][j];
                    String cellValue = cell.getValue();
                    int cellIsLocked = cell.isLocked() ? 1 : 0;

                    String insertQuery = "INSERT INTO board_state (game_id, cell_row, cell_column, value, static) " +
                                         "VALUES (" + gameID + ", " + i + ", " + j + ", '" + cellValue + "', " + cellIsLocked + ")";
                    statement.executeUpdate(insertQuery);
                }
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBoard() {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT cell_row, cell_column, value, static\n" +
                                 "FROM board_state\n" +
                                 "ORDER BY cell_row, cell_column;";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while(resultSet.next()) {
                int row = resultSet.getInt("cell_row");
                int col = resultSet.getInt("cell_column");
                String value = resultSet.getString("value");
                int isStatic = resultSet.getInt("static");

                Cell cell = gameBoard[row][col];
                cell.setLocked(false);
                cell.setValue(value);

                if (isStatic == 1) {
                    cell.setLocked(true);
                } else {
                    cell.setLocked(false);
                }
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("   ");

        for (int i = 1; i <= BOARD_SIZE; i++) {
            sb.append(i + "  ");
        }
        sb.append("\n");

        for (int i = 1; i <= BOARD_SIZE; i++) {
            sb.append(i + "  ");
            for (int j = 1; j <= BOARD_SIZE; j++) {
                sb.append(gameBoard[i][j].getValue() + "  ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
