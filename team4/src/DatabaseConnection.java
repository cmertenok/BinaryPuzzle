import java.sql.*;

public class DatabaseConnection {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:db/ascii4", "postgres", "Student_1234");
            System.out.println("Connection available!");
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS players"
                    + " (player_name VARCHAR(10), top_score INTEGER)");
            System.out.println("Table players created");
            statement.executeUpdate("DELETE FROM players");
            statement.executeUpdate("INSERT INTO players (player_name, top_score) VALUES ('Lars', 9001)");
            statement.executeUpdate("INSERT INTO players (player_name, top_score) VALUES ('Jack', 10000)");
            statement.executeUpdate("INSERT INTO players (player_name, top_score) VALUES ('Ben', 1234)");
            System.out.println("Data entered in table players");
            ResultSet resultSet = statement.executeQuery(
                    "SELECT player_name, top_score FROM players"
                            + " WHERE top_score > 9000");
            System.out.println("Following data retrieved from database:");
            while (resultSet.next()) {
                String playerName = resultSet.getString(1);
                int topScore = resultSet.getInt(2);
                System.out.printf("%-10s %5d%n", playerName, topScore);
            }
            statement.close(); connection.close();
            System.out.println("Connection closed!");
        } catch (SQLException exc) {
            exc.printStackTrace();
        }

    }
}
