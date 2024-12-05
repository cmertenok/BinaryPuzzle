import java.time.LocalDateTime;

public class GameSession {
    private int gameID;
    private Board board;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public GameSession() {
        this.gameID = -1;
        this.board = new Board();
        this.startTime = LocalDateTime.now();
    }
}


