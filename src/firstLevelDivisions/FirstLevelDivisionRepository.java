package firstLevelDivisions;

import database.DatabaseConnector;
import users.User;

import java.sql.Connection;
import java.sql.SQLException;

public class FirstLevelDivisionRepository {
    private Connection db;

    public FirstLevelDivisionRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public FirstLevelDivision fetchFirstLevelDivisionById(Integer id) throws SQLException {
        var ps = this.getDb().prepareStatement("SELECT * FROM first_level_divisions WHERE Division_ID = ?");
        ps.setInt(1, id);
        ps.setMaxRows(1);

        var rs = ps.executeQuery();

        var firstLevelDivision = new FirstLevelDivision();
        while (rs.next()) {
            firstLevelDivision.setDivisionId(rs.getInt("Division_ID"));
            firstLevelDivision.setDivision(rs.getString("Division"));
            firstLevelDivision.setCreateDate(rs.getTimestamp("Create_Date"));
            firstLevelDivision.setCreatedBy(rs.getString("Created_By"));
            firstLevelDivision.setLastUpdate(rs.getTimestamp("Last_Update"));
            firstLevelDivision.setLastUpdatedBy(rs.getString("Last_Updated_By"));
            firstLevelDivision.setCountryId(rs.getInt("COUNTRY_ID"));
        }

        if (firstLevelDivision.getDivisionId() == null) {
            return null;
        }

        return firstLevelDivision;
    }

    public Connection getDb() {
        return db;
    }
}
