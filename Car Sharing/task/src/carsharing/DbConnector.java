package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbConnector {
    private String DB_URL;

    DbConnector(String db) {
        this.DB_URL = "jdbc:h2:./src/carsharing/db/" + db;
    }

    public void createTables() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            String sql = """
                    CREATE TABLE IF NOT EXISTS COMPANY(
                      id INTEGER PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) UNIQUE NOT NULL);
                    """;
            stmt.executeUpdate(sql);

            sql = """
                CREATE TABLE IF NOT EXISTS car
                (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL,
                company_id INTEGER NOT NULL,
                CONSTRAINT fk_company FOREIGN KEY(company_id)
                REFERENCES company(id));
                    """;
            stmt.execute(sql);
            stmt.close();
            conn.close();
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public List<String> selectStrings(String query) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
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
