package carsharing;

import carsharing.dao.*;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private CompanyDao companyDao = new CompanyDaoImpl();
    private CarDao carDao = new CarDaoImpl();
    private CustomerDao customerDao = new CustomerDaoImpl();
    private int choice = 1;

    public void run() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");

        while(choice != 0){
            choice = getIntChoice();

            switch (choice) {
                case 1 -> managerMenu();
                case 2 -> customerList();
                case 3 -> createCustomer();
            }
        }
    }

    private void managerMenu() {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");

        choice = getIntChoice();
            switch (choice) {
                case 1 -> companyList();
                case 2 -> createCompany();
                case 0 -> run();
                }
    }

    private void createCompany() {
        System.out.println("Enter the company name:");

        String name = scanner.nextLine();
        companyDao.createCompany(new Company(name));
        System.out.println("The company was created!");
        managerMenu();
    }

    private void companyList() {
        List<Company> companies = companyDao.getCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose the company:");
            IntStream.range(0, companies.size())
                    .forEach(i -> System.out.printf("%s. %s%n", i + 1, companies.get(i).getName()));
            System.out.println("0. Back");

            choice = getIntChoice();

            if (choice <= companies.size() && choice > 0) {
                companyMenu(companies.get(choice - 1));
            }
        }
        managerMenu();
    }

    private int getIntChoice() {
        return Integer.parseInt(scanner.nextLine());
    }

    private void companyMenu(Company company) {
            System.out.println(String.format("'%s' company", company.getName()));
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");

            choice = getIntChoice();

            switch (choice) {
                case 1 -> carMenu(company);
                case 2 -> createCar(company);
                case 0 -> managerMenu();
            }
    }

    private void createCar(Company company) {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();

        carDao.createCar(new Car(name, company.getId()));
        System.out.println("The car was added!");
        companyMenu(company);
    }

    private void carMenu(Company company) {
        List<Car> cars = carDao.getCars(company.getId());

        if(cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            IntStream.range(0, cars.size())
                    .forEach(i -> System.out.printf("%s. %s%n", i + 1, cars.get(i).getName()));
        }
        companyMenu(company);
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();

        customerDao.createCustomer(new Customer(name));
        System.out.println("The customer was added!");
        System.out.println();
        run();
    }

    private void customerList() {
        List<Customer> customers = customerDao.getCustomers();

        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            System.out.println();
        } else {

            System.out.println("Customer list:");
            IntStream.range(0, customers.size())
                    .forEach(i -> System.out.printf("%s. %s%n", customers.get(i).getId(), customers.get(i).getName()));
            System.out.println("0. Back");

            int index = getIntChoice();

            if (index == 0) {
                run();
            }

            if (index <= customers.size() && index > 0) {
                customerMenu(customers.get(index - 1));
            }
        }
    }

    private void customerMenu(Customer customer){
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            choice = getIntChoice();

            switch (choice){
                case 1 -> rentCar(customer);
                case 2 -> returnCar(customer);
                case 3 -> showRentedCar(customer);
                case 0 -> run();
            }
    }

    private void returnCar(Customer customer) {
        if(customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            Car car = carDao.getCar(customer.getRentedCarId());
            carDao.updateCar(car);
            customerDao.setCar(customer, 0);
            System.out.println("You've returned a rented car!");
        }
    }

    private void showRentedCar(Customer customer) {
        if(customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
        } else {
            Car rentedCar = carDao.getCar(customer.getRentedCarId());
            Company company = companyDao.getCompany(rentedCar.getCompanyId());

            System.out.println("Your rented car:");
            System.out.println(rentedCar.getName());
            System.out.println("Company:");
            System.out.println(company.getName());
        }
        customerMenu(customer);
    }

    private void rentCar(Customer customer) {
        if (customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
            customerMenu(customer);
        }
        List<Company> companies = companyDao.getCompanies();

        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            customerMenu(customer);
        }

        System.out.println("Choose the company:");
        IntStream.range(0, companies.size())
                .forEach(i -> System.out.printf("%s. %s%n", i + 1, companies.get(i).getName()));
        System.out.println("0. Back");

        choice = getIntChoice();

        if (choice <= companies.size() && choice > 0) {
            List<Car> cars = carDao.getCars(choice);

            if (cars.isEmpty()) {
                System.out.println("The car list is empty!");
            } else {
                System.out.println("Choose a car:");
                IntStream.range(0, cars.size())
                        .forEach(i -> System.out.printf("%s. %s%n", i + 1, cars.get(i).getName()));
                }

            choice = getIntChoice();
            if (choice <= cars.size() && choice > 0) {
                Car car = cars.get(choice - 1);
                car.setRented(true);
                carDao.updateCar(car);
                customerDao.setCar(customer, car.getId());
                System.out.println("You rented '" + car.getName() + "'");
                customerMenu(customer);
            }
        }
    }
}