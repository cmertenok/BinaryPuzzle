public class Board {
    public static final int BOARD_SIZE = 8;
    public Cell[][] gameBoard;

    public Board() {
        this.gameBoard = new Cell[BOARD_SIZE + 1][BOARD_SIZE + 1];
        for (int i = 0; i < BOARD_SIZE + 1; i++) {
            for (int j = 0; j < BOARD_SIZE + 1; j++) {
                gameBoard[i][j] = new Cell();
            }
        }
    }

    public Cell getCellValue(int row, int col) {
        return gameBoard[row][col];
    }

    public void setCellValue(int row, int col, String value) {
        this.gameBoard[row][col].setValue(value);
    }

    public void loadBoard() {
        //TODO implement loading board
    }

    public void saveBoard() {
        //TODO implement saving board
    }

//    public void printBoard() {
//        System.out.print("\n   ");
//        for (int i = 1; i <= BOARD_SIZE; i++) {
//            System.out.print(i + "  ");
//        }
//        System.out.println();
//
//        for (int i = 1; i <= BOARD_SIZE; i++) {
//            System.out.print(i + "  ");
//            for (int j = 1; j <= BOARD_SIZE; j++) {
//                System.out.print(gameBoard[i][j].getValue() + "  ");
//            }
//            System.out.println();
//        }
//    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("\n   ");

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
