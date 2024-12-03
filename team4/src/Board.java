public class Board {
    public static final int BOARD_SIZE = 8;
    private Cell[][] gameBoard;

    public Board() {
        this.gameBoard = new Cell[BOARD_SIZE + 2][BOARD_SIZE + 2];
        for (int i = 0; i < BOARD_SIZE + 2; i++) {
            for (int j = 0; j < BOARD_SIZE + 2; j++) {
                gameBoard[i][j] = new Cell();
            }
        }
        createBorder();
    }

    public void setCellValue(int row, int col, String value) {
        this.gameBoard[row][col].setValue(value);
    }

    public boolean isEmpty(int row, int col) {
        return (gameBoard[row][col].equals('0') || gameBoard[row][col].equals('1')) ? true : false;
    }

    public void createBorder() {
        gameBoard[0][0].setValue("\u2554");
        gameBoard[0][BOARD_SIZE + 1].setValue("\u2557");
        gameBoard[BOARD_SIZE + 1][0].setValue("\u255A");
        gameBoard[BOARD_SIZE + 1][BOARD_SIZE + 1].setValue("\u255D");

        //vertical
        for (int i = 1; i < BOARD_SIZE + 1; i++) {
            gameBoard[0][i].setValue("\u2550");
            gameBoard[BOARD_SIZE + 1][i].setValue("\u2550");
        }

        //horizontal
        for (int i = 1; i < BOARD_SIZE + 1; i++) {
            gameBoard[i][0].setValue("\u2551");
            gameBoard[i][BOARD_SIZE + 1].setValue("\u2551");
        }

        //fill rest
        for (int i = 1; i <= BOARD_SIZE; i++) {
            for (int j = 1; j <= BOARD_SIZE; j++) {
                gameBoard[i][j].setValue("_");
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE + 2; i++) {
            for (int j = 0; j < BOARD_SIZE + 2; j++) {
                System.out.print(gameBoard[i][j].getValue() + "  ");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE + 2; i++) {
            for (int j = 0; j < BOARD_SIZE + 2; j++) {
                sb.append(gameBoard[i][j].getValue() + "  ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
