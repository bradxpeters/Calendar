package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Database connector.
 */
public class DatabaseConnector {

    private static final String connectionUrl = "jdbc:mysql://wgudb.ucertify.com/WJ07T8l?autoReconnect=true";

    private static final String username = "U07T8l";

    private static final String password = "53689123361";

    private static Connection db;

    /**
     * Gets instance.
     * <p>
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

