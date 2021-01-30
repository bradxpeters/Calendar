package users;

import database.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;

public class UserRepository {

    private Connection db;

    public UserRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public User fetchUserById(Integer id) throws SQLException {
        var ps = this.getDb().prepareStatement("SELECT * FROM users WHERE User_ID = ?");
        ps.setInt(1, id);
        ps.setMaxRows(1);

        var rs = ps.executeQuery();

        if (!rs.next()) {
            return null;
        }

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
        return user;
    }

    public Connection getDb() {
        return db;
    }
}
