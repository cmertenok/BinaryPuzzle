import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Player {
    private int playerID;
    private String name;
    private int moves;
    private GameSession gameSession;

    private final Scanner keyboard = new Scanner(System.in);

    public Player(String name) {
        this.name = name;
        this.moves = 0;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getMovesAmount() {
        return moves;
    }

    public boolean makeMove(Board board) {
        boolean validMove = false;

        while (!validMove) {
            try {
                System.out.print(GameMenu.BLUE + "Enter the row (" + GameMenu.RED + "'exit' " +
                        GameMenu.BLUE + "to quit, or " + GameMenu.YELLOW + "'save' " +
                        GameMenu.BLUE + "to save game): " + GameMenu.RESET);
                String input = keyboard.nextLine().trim().toLowerCase();

                if (input.equals("exit")) {
                    System.out.println(GameMenu.RED + "Exiting the game." + GameMenu.RESET);
                    return false;
                }

                if (input.equals("save")) {
                    gameSession.getBoard().saveBoard(gameSession.getGameID(), this.playerID);
                    System.out.println(GameMenu.GREEN + "Game has been saved!" + GameMenu.RESET);
                    return false;
                }

                int row = Integer.parseInt(input);

                System.out.print(GameMenu.BLUE + "Enter the column: " + GameMenu.RESET);
                int col = Integer.parseInt(keyboard.nextLine().trim());

                System.out.print(GameMenu.BLUE + "Enter the value (" + GameMenu.RED + "0 " + GameMenu.BLUE + "or" +
                        GameMenu.RED + " 1" + GameMenu.BLUE + "):" + GameMenu.RESET);
                String value = GameMenu.BLUE + keyboard.nextLine().trim() + GameMenu.RESET;

                if (!value.equals(GameMenu.BLUE + "0" + GameMenu.RESET) &&
                        !value.equals(GameMenu.BLUE + "1" + GameMenu.RESET)) {
                    System.out.println(GameMenu.RED + "Invalid value! Please enter " +
                            GameMenu.GREEN + "0" + GameMenu.RED + " or " +
                            GameMenu.GREEN + "1" + GameMenu.RESET);
                    continue;
                }

                validMove = board.validateMove(row, col, value);
                if (!validMove) {
                    System.out.println(GameMenu.RED + "Invalid move! Cell is locked or the coordinates are out of bounds." + GameMenu.RESET);
                } else {
                    moves++;
                    System.out.println(GameMenu.GREEN + "Move successful!" + GameMenu.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(GameMenu.RED + "Invalid input! Please enter a valid number or " +
                        GameMenu.GREEN + "'exit'" + GameMenu.RED + "/" + GameMenu.GREEN + "'save'" + GameMenu.RESET);
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format(GameMenu.GREEN + "\nSteps:" + GameMenu.RED + " %d" + GameMenu.RESET, moves);
    }
}
