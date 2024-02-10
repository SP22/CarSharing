package carsharing.dao;

import carsharing.DbConnector;
import carsharing.model.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private static final String SELECT_ALL = "SELECT * FROM company ORDER BY ID;";
    public static final String SELECT = "SELECT * FROM company WHERE id = ?";
    public static final String INSERT = "INSERT INTO company (name) VALUES (?)";

    @Override
    public List<Company> getCompanies() {
        try (Statement stmt = DbConnector.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(SELECT_ALL);
            List<Company> companies = new ArrayList<>();
            while (rs.next()) {
                companies.add(convertToCompany(rs));
            }
            return companies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCompany(Company company) {
        try (var stmt = DbConnector.getConnection().prepareStatement(INSERT)) {
            stmt.setString(1, company.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Company getCompany(int id) {
        try (var stmt = DbConnector.getConnection().prepareStatement(SELECT)) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return convertToCompany(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static Company convertToCompany(ResultSet rs) throws SQLException {
        return new Company(rs.getInt(1), rs.getString(2));
    }
}
