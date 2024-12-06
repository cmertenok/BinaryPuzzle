
public class Player {
    private int playerID;
    private String name;
    private int score;

    public Player(String name) {
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

    }

    public void increaseScore(int points) {
        this.score += points;
    }

    @Override
    public String toString() {
        return String.format("Score: %d", score);
    }
}
