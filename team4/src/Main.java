public class Main {
    public static void main(String[] args) {
        DatabaseConnection.main();
        GameMenu gameMenu = new GameMenu();
        gameMenu.start();
    }
}