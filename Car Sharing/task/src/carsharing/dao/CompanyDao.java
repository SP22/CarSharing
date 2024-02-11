package carsharing.dao;

import carsharing.model.Company;

import java.util.List;

public interface CompanyDao {
    List<Company> getCompanies();
    void createCompany(Company company);
    Company getCompany(int id);
}
