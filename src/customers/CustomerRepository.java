package customers;

import database.DatabaseConnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Customer> fetchAll() throws SQLException {
        var customers = new ArrayList<Customer>();

        var ps = this.getDb().prepareStatement(
            "SELECT * FROM customers"
        );

        var rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.toString());
            customers.add(this.fetchRsIntoCustomer(rs));
        }

        return customers;
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

    public Connection getDb() {
        return db;
    }
}

