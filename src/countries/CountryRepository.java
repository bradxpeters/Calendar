package countries;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CountryRepository {
    private Connection db;

    public CountryRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Country fetchCountryById(Integer id) throws SQLException {
        var ps = this.getDb().prepareStatement(
            "SELECT * FROM countries WHERE Country_ID = ?"
        );
        ps.setInt(1, id);
        ps.setMaxRows(1);

        var rs = ps.executeQuery();

        return this.fetchRsIntoCountry(rs);
    }

    public Country fetchCountryByDivisionId(Integer id) {

        var country = new Country();
        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM countries " +
                    "JOIN first_level_divisions fld ON countries.Country_ID = fld.COUNTRY_ID " +
                    "WHERE fld.Division_ID = ?"
            );
            ps.setInt(1, id);
            ps.setMaxRows(1);

            var rs = ps.executeQuery();
            country = this.fetchRsIntoCountry(rs);

        } catch (SQLException e) {
            System.out.println("Error getting country by division Id");
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return country;
    }

    public ObservableList<Country> fetchAll() {
        ObservableList<Country> countries = FXCollections.observableArrayList();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM countries"
            );

            var rs = ps.executeQuery();
            while (rs.next()) {
                countries.add(this.fetchRsIntoCountry(rs));
            }
        } catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return countries;
    }

    private Country fetchRsIntoCountry(ResultSet rs) throws SQLException {
        var country = new Country();

        country.setCountryId(rs.getInt("Country_ID"));
        country.setCountry(rs.getString("Country"));
        country.setCreateDate(rs.getTimestamp("Create_Date"));
        country.setCreatedBy(rs.getString("Created_By"));
        country.setLastUpdate(rs.getTimestamp("Last_Update"));
        country.setLastUpdatedBy(rs.getString("Last_Updated_By"));

        if (country.getCountryId() == null) {
            return null;
        }

        return country;
    }

    public Connection getDb() {
        return db;
    }
}



