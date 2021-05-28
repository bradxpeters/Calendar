package contacts;

import database.DatabaseConnector;
import users.User;

import java.sql.*;

public class ContactRepository {
    private Connection db;

    public ContactRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Contact fetchContactById(Integer id) {

        var contact = new Contact();

        try {
            var ps = this.getDb().prepareStatement("SELECT * FROM contacts WHERE Contact_ID = ?");
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            while (rs.next()) {
                contact = this.fetchRsIntoContact(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting contact by id");
            System.out.println(e.getMessage());
        }
        return contact;
    }

    public void fetchAll() {
        ContactList.getInstance().getContactList().clear();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM contacts"
            );

            var rs = ps.executeQuery();
            while (rs.next()) {
                ContactList.getInstance().getContactList().add(this.fetchRsIntoContact(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all contacts");
            System.out.println(e.getMessage());
        }
    }

    private Contact fetchRsIntoContact(ResultSet rs) throws SQLException {
        var contact = new Contact();

        contact.setContactId(rs.getInt("Contact_ID"));
        contact.setContactName(rs.getString("Contact_Name"));
        contact.setEmail(rs.getString("Email"));

        if (contact.getContactId() == null) {
            return null;
        }

        return contact;
    }

    public void createOrUpdateContact(Contact contact) {
        String sql =
            "INSERT INTO contacts (" +
                "Contact_ID, Contact_Name, Email) " +
            "VALUES (?, ?, ?)" +
            "ON DUPLICATE KEY UPDATE " +
                "Contact_ID=VALUES(Contact_ID), " +
                "Contact_Name=VALUES(Contact_Name), " +
                "Email=VALUES(Email)";
        try {
            PreparedStatement ps = this.getDb().prepareStatement(sql);
            ps.setObject(1,contact.getContactId());
            ps.setString(2, contact.getContactName());
            ps.setString(3, contact.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating or updating contact " + contact.getContactName());
            System.out.println(e.getMessage());
        }

        this.fetchAll();
    }

    public void deleteContact(Contact contact) {
        // Clean up appointments
        var appointmentSql = "DELETE FROM appointments WHERE Contact_ID = ?";

        // Delete contact
        var contactSql = "DELETE FROM contacts WHERE Contact_ID = ?";

        try {
            var ps1 = this.getDb().prepareStatement(appointmentSql);
            var ps2 = this.getDb().prepareStatement(contactSql);

            ps1.setObject(1,contact.getContactId());
            ps2.setObject(1,contact.getContactId());

            ps1.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting contact: " + contact.getContactId());
            System.out.println(e.getMessage());
        }

        this.fetchAll();
    }

    public Connection getDb() {
        return db;
    }
}


