public class Main {
    public static void main(String[] args) {
//        System.out.println("| ___ (_)                       \n" +
//                "| |_/ /_ _ __   __ _ _ __ _   _ \n" +
//                "| ___ \\ | '_ \\ / _` | '__| | | |\n" +
//                "| |_/ / | | | | (_| | |  | |_| |\n" +
//                "\\____/|_|_| |_|\\__,_|_|   \\__, |\n" +
//                "                           __/ |\n" +
//                "                          |___/ \n" +
//                "______              _           \n" +
//                "| ___ \\            | |          \n" +
//                "| |_/ /   _ _______| | ___      \n" +
//                "|  __/ | | |_  /_  / |/ _ \\     \n" +
//                "| |  | |_| |/ / / /| |  __/     \n" +
//                "\\_|   \\__,_/___/___|_|\\___|     \n");

        Board board = new Board();
        Player player = new Player(1, "Artem");
//        player.makeMove(2, 4, "0");
        board.printBoard();

    }
}