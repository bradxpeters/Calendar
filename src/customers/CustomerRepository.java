package customers;

import authorization.AuthorizedState;
import database.DatabaseConnector;
import reports.ReportsRepository;

import java.sql.*;

public class CustomerRepository {
    private Connection db;

    public CustomerRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Customer fetchCustomerById(Integer id) {

        var customer = new Customer();

        if (id == null) {
            return customer;
        }

        try {
            var ps = this.getDb().prepareStatement("" +
                "SELECT customers.*, fld.Division AS divisionName, c.Country as countryName " +
                "FROM customers " +
                "JOIN first_level_divisions fld ON customers.Division_ID = fld.Division_ID " +
                "JOIN countries c ON c.Country_ID = fld.COUNTRY_ID " +
                "WHERE customers.Customer_ID = ?"
            );
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            while (rs.next()) {
                customer = this.fetchRsIntoCustomer(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting customer by id");
            System.out.println(e.getMessage());
        }
        return customer;
    }

    public void fetchAll() {
        CustomerList.getInstance().getCustomerList().clear();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT customers.*, fld.Division AS divisionName, c.Country as countryName " +
                    "FROM customers " +
                    "JOIN first_level_divisions fld ON customers.Division_ID = fld.Division_ID " +
                    "JOIN countries c ON c.Country_ID = fld.COUNTRY_ID " +
                    "ORDER BY customers.Customer_ID ASC"
            );

            var rs = ps.executeQuery();
            while (rs.next()) {
                CustomerList.getInstance().getCustomerList().add(this.fetchRsIntoCustomer(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all customers");
            System.out.println(e.getMessage());
        }
    }

    private Customer fetchRsIntoCustomer(ResultSet rs) throws SQLException {
        var customer = new Customer();

        customer.setCustomerId(rs.getInt("Customer_ID"));
        customer.setCustomerName(rs.getString("Customer_Name"));
        customer.setAddress(rs.getString("Address"));
        customer.setPostalCode(rs.getString("Postal_Code"));
        customer.setPhone(rs.getString("Phone"));
        customer.setCreateDate(rs.getTimestamp("Create_Date"));
        customer.setCreatedBy(rs.getString("Created_By"));
        customer.setCreateDate(rs.getTimestamp("Last_Update"));
        customer.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        customer.setDivisionId(rs.getInt("Division_ID"));
        customer.setDivisionName(rs.getString("divisionName"));
        customer.setCountryName(rs.getString("countryName"));

        if (customer.getCustomerId() == null) {
            return null;
        }

        return customer;
    }

    public void createOrUpdateCustomer(Customer customer) {
        String sql =
            "INSERT INTO customers (" +
                "Customer_ID, Customer_Name, Address, Postal_Code, Phone, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE " +
                "Customer_ID=VALUES(Customer_ID), " +
                "Customer_Name=VALUES(Customer_Name), " +
                "Address=VALUES(Address)," +
                "Postal_Code=VALUES(Postal_Code)," +
                "Phone=VALUES(Phone)," +
                "Last_Update=VALUES(Last_Update)," +
                "Last_Updated_By=VALUES(Last_Updated_By)," +
                "Division_ID=VALUES(Division_ID)";
        try {
            PreparedStatement ps = this.getDb().prepareStatement(sql);
            ps.setObject(1, customer.getCustomerId());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPostalCode());
            ps.setString(5, customer.getPhone());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setString(7, AuthorizedState.getInstance().getAuthorizedUser().getUserName());
            ps.setInt(8, customer.getDivisionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating or updating customer " + customer.getCustomerName());
            System.out.println(e.getMessage());
        }

        this.fetchAll();
        var reportRepository = new ReportsRepository();
        reportRepository.refreshAllReports();
    }

    public void deleteCustomer(Customer customer) {
        // Clean up appointments
        var appointmentSql = "DELETE FROM appointments WHERE Customer_ID = ?";

        // Delete customer
        var customerSQL = "DELETE FROM customers WHERE Customer_ID = ?";

        try {
            var ps1 = this.getDb().prepareStatement(appointmentSql);
            var ps2 = this.getDb().prepareStatement(customerSQL);

            ps1.setObject(1, customer.getCustomerId());
            ps2.setObject(1, customer.getCustomerId());

            ps1.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + customer.getCustomerId());
            System.out.println(e.getMessage());
        }

        this.fetchAll();
    }

    public Connection getDb() {
        return db;
    }
}

