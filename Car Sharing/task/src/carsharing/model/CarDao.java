package carsharing.model;

import java.util.List;

public interface CarDao {
    List<Car> getCars(int companyId);
    void createCar(int companyId, String name);
}
