package carsharing.model;

import java.util.List;

import carsharing.DbConnector;

public class CarDaoImpl implements CarDao {
    private static final String SELECT_ALL = "SELECT name FROM car WHERE company_id = %s;";
    public static final String INSERT = "INSERT INTO car (name, company_id) VALUES ('%s', %s);";
    private final DbConnector db;

    public CarDaoImpl(DbConnector db) {
        this.db = db;
    }

    @Override
    public List<Car> getCars(int companyId) {
        return db.selectStrings(String.format(SELECT_ALL, companyId))
                .stream()
                .map(Car::new)
                .toList();
    }

    @Override
    public void createCar(int companyId, String name) {
        db.executeInsert(String.format(INSERT, name, companyId));
    }

}
