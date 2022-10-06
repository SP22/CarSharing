package carsharing.model;

import java.util.List;

public interface CompanyDao {
    List<Company> getCompanies();
    void createCompany(String name);
    int getCompanyId(String name);
}
