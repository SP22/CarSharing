package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbConnector {

    private static Connection connection = null;
    private static String DB_URL;

    DbConnector(String db) {
        DB_URL = "jdbc:h2:./src/carsharing/db/" + db;
    }

    public void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()
        ) {

            conn.setAutoCommit(true);
            String sql = """
                    CREATE TABLE IF NOT EXISTS COMPANY(
                      id INTEGER PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) UNIQUE NOT NULL);
                    """;
            stmt.execute(sql);

            sql = """
                CREATE TABLE IF NOT EXISTS car(
                id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL,
                company_id INTEGER NOT NULL,
                is_rented BOOLEAN NOT NULL DEFAULT false,
                CONSTRAINT fk_company FOREIGN KEY(company_id)
                REFERENCES company(id));
                    """;
            stmt.execute(sql);

            sql = """
                CREATE TABLE IF NOT EXISTS customer(
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) UNIQUE NOT NULL,
                rented_car_id INT NULL,
                CONSTRAINT fk_car FOREIGN KEY (rented_car_id) REFERENCES car(id));""";
            stmt.execute(sql);
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        connection = null;
    }

    public List<String> selectStrings(String query) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()
        ) {
            conn.setAutoCommit(true);
            ResultSet rs = stmt.executeQuery(query);
            List<String> strings = new ArrayList<>();
            while (rs.next()) {
                strings.add(rs.getString(1));
            }
            return strings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeInsert(String query) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(true);
            stmt.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
