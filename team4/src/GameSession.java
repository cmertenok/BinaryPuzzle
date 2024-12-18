import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class GameSession {
    private int gameID;
    private Board board;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public GameSession() {
        this.board = new Board();
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void updateSessionDuration() {
        try {
            Connection connection = DriverManager.getConnection(DatabaseConnection.url, DatabaseConnection.user, DatabaseConnection.password);
            Statement statement = connection.createStatement();

            String selectQuery = "SELECT session_duration FROM game_info WHERE game_id = " + gameID;
            ResultSet resultSet = statement.executeQuery(selectQuery);
            int currentDuration = 0;

            if (resultSet.next()) {
                currentDuration = resultSet.getInt("session_duration");
            }

            LocalDateTime now = LocalDateTime.now();
            long newDuration = Duration.between(startTime, now).getSeconds();
            int totalDuration = currentDuration + (int) newDuration;
            String updateQuery = "UPDATE game_info SET session_duration = " + totalDuration + " WHERE game_id = " + gameID;
            statement.executeUpdate(updateQuery);

            endTime = now;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isBoardComplete() {
        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            for (int j = 1; j <= Board.BOARD_SIZE; j++) {
                if (board.getCellValue(i, j).isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    public String removeColor(int row, int col) {
        String value = board.getCellValue(row, col) + "";
        return value.substring(5, value.length() - 4);
    }

    public boolean validateAmount() {
        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            int rowZeroCount = 0;
            int rowOneCount = 0;
            int colZeroCount = 0;
            int colOneCount = 0;

            for (int j = 1; j <= Board.BOARD_SIZE; j++) {
                if (removeColor(i, j).equals("0")) {
                    rowZeroCount++;
                } else if (removeColor(i, j).equals("1")) {
                    rowOneCount++;
                }

                if (removeColor(j, i).equals("0")) {
                    colZeroCount++;
                } else if (removeColor(j, i).equals("1")) {
                    colOneCount++;
                }
            }

            if (rowZeroCount != rowOneCount || colZeroCount != colOneCount) {
                return false;
            }
        }

        return true;
    }

    public boolean validateSequence() {
        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            for (int j = 1; j <= Board.BOARD_SIZE - 2; j++) {
                if (removeColor(i, j) == removeColor(i, j + 1) &&
                    removeColor(i, j) == removeColor(i, j + 2)) {
                    return false;
                }

                if (removeColor(j, i) == removeColor(j + 1, i) &&
                    removeColor(j, i) == removeColor(j + 2, i)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean validateUniqueness() {
        Set<String> uniqueRows = new HashSet<>();
        Set<String> uniqueColumns = new HashSet<>();

        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            StringBuilder row = new StringBuilder();
            StringBuilder col = new StringBuilder();

            for (int j = 1; j <= Board.BOARD_SIZE; j++) {
                row.append(removeColor(i, j));
                col.append(removeColor(j, i));
            }

            uniqueRows.add(row.toString());
            uniqueColumns.add(col.toString());
        }

        return (uniqueColumns.size() == Board.BOARD_SIZE) && (uniqueRows.size() == Board.BOARD_SIZE);
    }

    @Override
    public String toString() {
        return String.format("GameID: %d, Date %s", gameID, startTime);
    }
}
