package carsharing;

import java.util.List;
import java.util.Scanner;

import carsharing.model.Car;
import carsharing.model.CarDao;
import carsharing.model.CarDaoImpl;
import carsharing.model.Company;
import carsharing.model.CompanyDao;
import carsharing.model.CompanyDaoImpl;

public class Manager {

    private Scanner scanner = new Scanner(System.in);
    private CompanyDao companyDao;
    private CarDao carDao;

    public Manager(DbConnector db) {
        this.companyDao = new CompanyDaoImpl(db);
        this.carDao = new CarDaoImpl(db);
    }

    public void loginAsManager() {
        int action = -1;
        while (action != 0) {
            printManagerMenu();
            action = Integer.valueOf(scanner.nextLine());
            switch (action) {
                case 1:
                    companyMenu();
                    break;
                case 2:
                    createCompany();
                    break;
                case 0:
                default:
                    break;
            }
        }
    }

    private void printManagerMenu() {
        System.out.println("""
                1. Company list
                2. Create a company
                0. Back
                """);
    }

    private void printCarMenu() {
        System.out.println("""
                1. Car list
                2. Create a car
                0. Back
                """);
    }

    private void companyMenu() {
        List<Company> companies = companyDao.getCompanies();
        int companyIndex = -1;
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            while (companyIndex != 0) {
                printCompanies(companies);
                companyIndex = Integer.valueOf(scanner.nextLine());
                if (companyIndex < 0 || companyIndex > companies.size()) {
                    System.out.println("No such company!");
                } else if (companyIndex != 0) {
                    manageCompany(companies.get(companyIndex - 1));
                }
            }
        }
    }

    private void printCompanies(List<Company> companies) {
        System.out.println("Choose the company:");
        int index = 0;
        for (var i : companies) {
            System.out.println(++index + ". " + i.getName());
        }
        System.out.println("0. Back");
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        companyDao.createCompany(name);
        System.out.println("The company was created!");
    }

    private void manageCompany(Company company) {
        int companyId = companyDao.getCompanyId(company.getName());

        System.out.printf("'%s' company%n", company.getName());
        int action = -1;
        while (action != 0) {
            printCarMenu();
            action = Integer.valueOf(scanner.nextLine());
            switch (action) {
                case 0:
                    break;
                case 1:
                    printCars(companyId);
                    break;
                case 2:
                    createCar(companyId);
                    break;
                default:
                    System.out.println("No such option");
            }
        }
    }

    private void printCars(int companyId) {
        List<Car> cars = carDao.getCars(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            int index = 0;
            for (var i : cars) {
                System.out.println(++index + ". " + i.getName());
            }
        }
    }

    private void createCar(int companyId) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        carDao.createCar(companyId, carName);
        System.out.println("The car was added!");
    }

}
