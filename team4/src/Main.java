public class Main {
    public static void main(String[] args) {
        DatabaseConnection.setupDatabase();
        GameMenu gameMenu = new GameMenu();
        gameMenu.start();
    }
}