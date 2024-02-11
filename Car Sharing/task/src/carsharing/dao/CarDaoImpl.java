package carsharing.dao;

import carsharing.DbConnector;
import carsharing.model.Car;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {

    private static final String SELECT_ALL = "SELECT * FROM car WHERE company_id = ? ORDER BY ID";
    public static final String SELECT_NOT_RENTED = "SELECT * FROM car WHERE company_id = ? AND is_rented = false ORDER BY ID";
    public static final String SELECT = "SELECT * FROM car WHERE id = ?";
    public static final String INSERT = "INSERT INTO car (name, company_id) VALUES (?, ?)";
    public static final String UPDATE_RENTED = "UPDATE car set is_rented = ? WHERE id = ?";

    @Override
    public List<Car> getCars(int companyId) {
        try (var stmt = DbConnector.getConnection().prepareStatement(SELECT_ALL)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(convertToCar(rs));
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getNotRentedCars(int companyId) {
        try (var stmt = DbConnector.getConnection().prepareStatement(SELECT_NOT_RENTED)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(convertToCar(rs));
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car getCar(int id) {
        try (var stmt = DbConnector.getConnection().prepareStatement(SELECT)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return convertToCar(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void createCar(Car car) {
        try (var stmt = DbConnector.getConnection().prepareStatement(INSERT)) {
            stmt.setString(1, car.getName());
            stmt.setInt(2, car.getCompanyId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCar(Car car) {
        try (var stmt = DbConnector.getConnection().prepareStatement(UPDATE_RENTED)) {
            stmt.setBoolean(1, car.isRented());
            stmt.setInt(2, car.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Car convertToCar(ResultSet rs) throws SQLException {
        return new Car(rs.getInt(1), rs.getString(2), rs.getInt(3));
    }
}
