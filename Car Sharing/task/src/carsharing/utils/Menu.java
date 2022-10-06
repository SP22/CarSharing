package carsharing.utils;

import java.util.Scanner;

import carsharing.DbConnector;
import carsharing.Manager;

public class Menu {
    public void run(DbConnector db) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager(db);
        int action = 1;
        while (action != 0) {
            printMainMenu();
            action = Integer.valueOf(scanner.next());
            switch (action) {
                case 1:
                    manager.loginAsManager();
                    break;
                case 0:
                    break;
            }
        }
    }

    private void printMainMenu() {
        System.out.println("""
                1. Log in as a manager
                0. Exit
                """);
    }

}
