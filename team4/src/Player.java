import java.util.Scanner;

public class Player {
    private int playerID;
    private String name;
    private int score;

    private Board board;
    private final Scanner scanner = new Scanner(System.in);

    public Player(int playerID, String name) {
        this.playerID = playerID;
        this.name = name;
        this.score = 0;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void makeMove(int row, int col, String value) {
        boolean validMove = false;
        value = scanner.nextLine();
        do {
            if ((row < 0 || row > Board.BOARD_SIZE) || (col < 0 || col > Board.BOARD_SIZE)) {
                System.out.println("Wrong cell!");
            } else {
                if(value.equals("1") || value.equals("2")) {
                    board.setCellValue(row, col, value);
                    increaseScore(10);
                    validMove = true;
                } else {
                    System.out.println("Invalid character!");
                }
            }
        } while (!validMove);
    }

    public void increaseScore(int points) {
        this.score += points;
    }

    @Override
    public String toString() {
        return String.format("%s - %d", name, score);
    }
}
