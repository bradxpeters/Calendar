package contacts;

import database.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Contact repository.
 */
public class ContactRepository {
    private Connection db;

    /**
     * Instantiates a new Contact repository.
     */
    public ContactRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Fetch contact by id contact.
     *
     * @param id the id
     * @return the contact
     */
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

    /**
     * Fetch all.
     */
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

    /**
     * Gets db.
     *
     * @return the db
     */
    public Connection getDb() {
        return db;
    }
}


