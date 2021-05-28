package customers;

import database.DatabaseConnector;
import users.User;

import java.sql.*;
import java.util.Arrays;

public class CustomerRepository {
    private Connection db;

    public CustomerRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Customer fetchCustomerById(Integer id) throws SQLException {
        var ps = this.getDb().prepareStatement("SELECT * FROM customers WHERE Customer_ID = ?");
        ps.setInt(1, id);
        ps.setMaxRows(1);

        var rs = ps.executeQuery();

        return this.fetchRsIntoCustomer(rs);
    }

    public void fetchAll() {
        CustomerList.getInstance().getCustomerList().clear();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM customers"
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

        if (customer.getCustomerId() == null) {
            return null;
        }

        return customer;
    }

    public void createOrUpdateCustomer(Customer customer, User user) {
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
            ps.setObject(1,customer.getCustomerId());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPostalCode());
            ps.setString(5, customer.getPhone());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setString(7, user.getUserName());
            ps.setInt(8, customer.getDivisionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating or updating customer " + customer.getCustomerName());
            System.out.println(e.getMessage());
        }

        this.fetchAll();
    }

    public Connection getDb() {
        return db;
    }
}

