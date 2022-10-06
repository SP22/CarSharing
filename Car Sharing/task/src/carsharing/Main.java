package carsharing;

import carsharing.utils.Menu;

public class Main {
    public static void main(String[] args) {
        DbConnector db = new DbConnector(args[1]);
        db.createTables();

        Menu menu = new Menu();
        menu.run(db);
    }
}