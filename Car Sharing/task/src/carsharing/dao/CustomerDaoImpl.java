package carsharing.dao;

import carsharing.DbConnector;
import carsharing.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    private static final String SELECT_ALL = "SELECT * FROM customer ORDER BY ID;";
    public static final String SELECT = "SELECT * FROM customer WHERE id = ?";

    public static final String UPDATE_CAR = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
    public static final String INSERT = "INSERT INTO customer (name) VALUES (?)";
    @Override
    public List<Customer> getCustomers() {
        try (Statement stmt = DbConnector.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(SELECT_ALL);
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(convertToCustomer(rs));
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCustomer(Customer customer) {
        try (var stmt = DbConnector.getConnection().prepareStatement(INSERT)) {
            stmt.setString(1, customer.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getCustomer(int id) {
        try (var stmt = DbConnector.getConnection().prepareStatement(SELECT)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return convertToCustomer(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setCar(Customer customer, int i) {
        try (var stmt = DbConnector.getConnection().prepareStatement(UPDATE_CAR)) {
            if (i == 0) {
                stmt.setString(1, null);
            } else {
                stmt.setInt(1, i);
            }
            stmt.setInt(2, customer.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Customer convertToCustomer(ResultSet rs) throws SQLException {
        return new Customer(rs.getInt(1), rs.getString(2), rs.getInt(3));
    }
}
