package database;

import java.sql.*;

/**
 * The type Database connector.
 */
public class DatabaseConnector {

    private static final String connectionUrl = "jdbc:mysql://wgudb.ucertify.com/WJ07T8l";

    private static final String username = "U07T8l";

    private static final String password = "53689123361";

    private static Connection db;

    private DatabaseConnector(Connection db) {}

    /**
     * Gets instance.
     *
     * Improvement: Extract the password to a config file outside of the code.
     *
     * @return the instance
     * @throws SQLException the sql exception
     */
    public static Connection getInstance() throws SQLException {
        if (db == null) {
            return DriverManager.getConnection(connectionUrl, username, password);
        }
        return db;
    }


}

