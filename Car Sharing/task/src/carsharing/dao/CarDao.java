package carsharing.dao;

import carsharing.model.Car;

import java.util.List;

public interface CarDao {
    List<Car> getCars(int companyId);

    void createCar(Car car);

    void updateCar(Car car);

    Car getCar(int id);
}
