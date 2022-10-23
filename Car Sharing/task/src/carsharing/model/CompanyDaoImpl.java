package carsharing.model;

import java.util.List;

import carsharing.DbConnector;

public class CompanyDaoImpl implements CompanyDao {
    private static final String SELECT_ALL = "SELECT name FROM company ORDER BY ID;";
    public static final String SELECT_ID = "SELECT id FROM company WHERE name = '%s';";
    public static final String INSERT = "INSERT INTO company (name) VALUES ('%s');";
    private final DbConnector db;

    public CompanyDaoImpl(DbConnector db) {
        this.db = db;
    }

    @Override
    public List<Company> getCompanies() {
        return db.selectStrings(SELECT_ALL)
                .stream()
                .map(Company::new)
                .toList();
    }

    @Override
    public void createCompany(String name) {
        db.executeInsert(String.format(INSERT, name));
    }

    @Override
    public int getCompanyId(String name) {
        return Integer.valueOf(db.selectStrings(String.format(SELECT_ID, name))
                .get(0)
        );
    }

}
