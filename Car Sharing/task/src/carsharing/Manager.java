package carsharing;

import java.util.List;
import java.util.Scanner;

import carsharing.model.Company;
import carsharing.model.CompanyDao;
import carsharing.model.CompanyDaoImpl;

public class Manager {

    private Scanner scanner = new Scanner(System.in);
    private CompanyDao companyDao;

    public Manager(DbConnector db) {
        this.companyDao = new CompanyDaoImpl(db);
    }

    public void loginAsManager() {
        int action;
        do {
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
        } while (action != 0);
    }

    private static void printManagerMenu() {
        System.out.println("""
                1. Company list
                2. Create a company
                0. Back
                """);
    }

    private void companyMenu() {
        List<Company> companies = companyDao.getCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            printCompanies(companies);
        }
    }

    private void printCompanies(List<Company> companies) {
        System.out.println("Company list:");
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

}
