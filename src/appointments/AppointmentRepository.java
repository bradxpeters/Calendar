package appointments;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class AppointmentRepository {
    private Connection db;

    public AppointmentRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Appointment fetchAppointmentById(Integer id) throws SQLException {
        var ps = this.getDb().prepareStatement(
            "SELECT * FROM appointments WHERE Appointment_ID = ?"
        );
        ps.setInt(1, id);
        ps.setMaxRows(1);

        var rs = ps.executeQuery();

        return this.fetchRsIntoAppointment(rs);
    }

    public ObservableList<Appointment> fetchAll() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM appointments"
            );

            var rs = ps.executeQuery();
            while (rs.next()) {
                appointments.add(this.fetchRsIntoAppointment(rs));
            }
        } catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        return appointments;
    }

    private Appointment fetchRsIntoAppointment(ResultSet rs) throws SQLException {
        var appointment = new Appointment();

        appointment.setAppointmentId(rs.getInt("Appointment_ID"));
        appointment.setTitle(rs.getString("Title"));
        appointment.setDescription(rs.getString("Description"));
        appointment.setLocation(rs.getString("Location"));
        appointment.setType(rs.getString("Type"));
        appointment.setStart(rs.getTimestamp("Start"));
        appointment.setEnd(rs.getTimestamp("End"));
        appointment.setCreateDate(rs.getTimestamp("Create_Date"));
        appointment.setCreatedBy(rs.getString("Created_By"));
        appointment.setLastUpdate(rs.getTimestamp("Last_Update"));
        appointment.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        appointment.setCustomerId(rs.getInt("Customer_ID"));
        appointment.setUserId(rs.getInt("User_ID"));
        appointment.setContactId(rs.getInt("Contact_Id"));

        if (appointment.getCustomerId() == null) {
            return null;
        }

        return appointment;
    }

    public Connection getDb() {
        return db;
    }
}


