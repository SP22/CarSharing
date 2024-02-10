package carsharing.dao;

import carsharing.model.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> getCustomers();
    void createCustomer(Customer customer);
    Customer getCustomer(int id);

    void setCar(Customer customer, int i);
}
