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
                System.out.print(Color.BLUE + "Enter the row (" + Color.RED + "'exit' " +
                                 Color.BLUE + "to quit, or " + Color.YELLOW + "'save' " +
                                 Color.BLUE + "to save game): " + Color.RESET);
                String input = keyboard.nextLine().trim().toLowerCase();

                if (input.equals("exit")) {
                    System.out.println(Color.RED + "Exiting the game." + Color.RESET);
                    return false;
                }

                if (input.equals("save")) {
                    gameSession.getBoard().saveBoard(gameSession.getGameID());
                    System.out.println(Color.GREEN + "Game has been saved!" + Color.RESET);
                    return false;
                }

                int row = Integer.parseInt(input);

                System.out.print(Color.BLUE + "Enter the column: " + Color.RESET);
                int col = Integer.parseInt(keyboard.nextLine().trim());

                System.out.print(Color.BLUE + "Enter the value (" + Color.RED + "0 " + Color.BLUE + "or" +
                                 Color.RED + " 1" + Color.BLUE + "):" + Color.RESET);
                String value = Color.BLUE + keyboard.nextLine().trim() + Color.RESET;

                if (!value.equals(Color.BLUE + "0" + Color.RESET) && !value.equals(Color.BLUE + "1" + Color.RESET)) {
                    System.out.println(Color.RED + "Invalid value! Please enter " + Color.GREEN + "0" +
                                       Color.RED + " or " + Color.GREEN + "1" + Color.RESET);
                    continue;
                }

                validMove = board.validateMove(row, col, value);
                if (!validMove) {
                    System.out.println(Color.RED + "Invalid move! Cell is locked or the coordinates are out of bounds." + Color.RESET);
                } else {
                    moves++;
                    System.out.println(Color.GREEN + "Move successful!" + Color.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Color.RED + "Invalid input! Please enter a valid number or " +
                                   Color.GREEN + "'exit'" + Color.RED + "/" + Color.GREEN + "'save'" + Color.RESET);
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return String.format(Color.GREEN + "\nSteps:" + Color.RED + " %d" + Color.RESET, moves);
    }
}
