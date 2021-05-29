package users;

import database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The type User repository.
 */
public class UserRepository {

    private Connection db;

    /**
     * Instantiates a new User repository.
     */
    public UserRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Fetch user by username user.
     *
     * @param username the username
     * @return the user
     * @throws SQLException the sql exception
     */
    public User fetchUserByUsername(String username) throws SQLException {
        var ps = this.getDb().prepareStatement("SELECT * FROM users WHERE User_Name = ?");
        ps.setString(1, username);
        return getUser(ps);
    }

    private User getUser(PreparedStatement ps) throws SQLException {
        ps.setMaxRows(1);

        var rs = ps.executeQuery();

        var user = new User();
        while (rs.next()) {
            user.setUserId(rs.getInt("User_ID"));
            user.setUserName(rs.getString("User_Name"));
            user.setPassword(rs.getString("Password"));
            user.setCreateDate(rs.getTimestamp("Create_Date"));
            user.setCreatedBy(rs.getString("Created_By"));
            user.setLastUpdate(rs.getTimestamp("Last_Update"));
            user.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        }

        if (user.getUserId() == null) {
            return null;
        }
        return user;
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
