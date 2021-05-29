package firstLevelDivisions;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * The type First level division repository.
 */
public class FirstLevelDivisionRepository {
    private Connection db;

    /**
     * Instantiates a new First level division repository.
     */
    public FirstLevelDivisionRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Fetch first level division by id first level division.
     *
     * @param id the id
     * @return the first level division
     */
    public FirstLevelDivision fetchFirstLevelDivisionById(Integer id) {

        var firstLevelDivision = new FirstLevelDivision();
        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM first_level_divisions WHERE first_level_divisions.Division_ID = ?"
            );
            ps.setInt(1, id);
            ps.setMaxRows(1);

            var rs = ps.executeQuery();
            while (rs.next()) {
                firstLevelDivision = this.fetchRsIntoFirstLevelDivision(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching first level division by id: " + id);
            System.out.println(e.getMessage());
        }

        return firstLevelDivision;
    }

    /**
     * Fetch first level divisions by country id observable list.
     *
     * @param id the id
     * @return the observable list
     */
    public ObservableList<FirstLevelDivision> fetchFirstLevelDivisionsByCountryId(Integer id) {

        ObservableList<FirstLevelDivision> firstLevelDivisions = FXCollections.observableArrayList();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM first_level_divisions WHERE first_level_divisions.COUNTRY_ID = ?"
            );
            ps.setInt(1, id);

            var rs = ps.executeQuery();
            while (rs.next()) {
                firstLevelDivisions.add(this.fetchRsIntoFirstLevelDivision(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching first level division by country id: " + id);
            System.out.println(e.getMessage());
        }

        return firstLevelDivisions;
    }


    private FirstLevelDivision fetchRsIntoFirstLevelDivision(ResultSet rs) throws SQLException {
        var firstLevelDivision = new FirstLevelDivision();

        firstLevelDivision.setDivisionId(rs.getInt("Division_ID"));
        firstLevelDivision.setDivision(rs.getString("Division"));
        firstLevelDivision.setCreateDate(rs.getTimestamp("Create_Date"));
        firstLevelDivision.setCreatedBy(rs.getString("Created_By"));
        firstLevelDivision.setLastUpdate(rs.getTimestamp("Last_Update"));
        firstLevelDivision.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        firstLevelDivision.setCountryId(rs.getInt("COUNTRY_ID"));

        if (firstLevelDivision.getDivisionId() == null) {
            return null;
        }

        return firstLevelDivision;
    }

    /**
     * Fetch all observable list.
     *
     * @return the observable list
     */
    public ObservableList<FirstLevelDivision> fetchAll() {
        ObservableList<FirstLevelDivision> firstLevelDivisions = FXCollections.observableArrayList();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM appointments"
            );

            var rs = ps.executeQuery();
            while (rs.next()) {
                firstLevelDivisions.add(this.fetchRsIntoFirstLevelDivision(rs));
            }
        } catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return firstLevelDivisions;
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
