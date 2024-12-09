import java.util.Random;

public class Board {
    public static final int BOARD_SIZE = 8;
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

    public void setCellValue(int row, int col, String value) {
        this.gameBoard[row][col].setValue(value);
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

    public void loadBoard() {
        //TODO implement loading board
    }

    public void saveBoard() {
        //TODO implement saving board
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
