package appointments;

import authorization.AuthorizedState;
import database.DatabaseConnector;
import reports.ReportsRepository;

import java.sql.*;
import java.time.ZonedDateTime;

public class AppointmentRepository {
    private Connection db;

    public AppointmentRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Appointment fetchAppointmentById(Integer id) {
        var appointment = new Appointment();

        if (id == null) {
            return appointment;
        }

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM appointments WHERE Appointment_ID = ?"
            );
            ps.setInt(1, id);
            ps.setMaxRows(1);
            var rs = ps.executeQuery();

            while (rs.next()) {
                appointment = this.fetchRsIntoAppointment(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching appointment by id: " + id);
            System.out.println(e.getMessage());
        }

        return appointment;
    }

    public void fetchAll() {
        AppointmentList.getInstance().getAppointmentList().clear();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM appointments"
            );

            var rs = ps.executeQuery();
            while (rs.next()) {
                AppointmentList.getInstance().getAppointmentList().add(this.fetchRsIntoAppointment(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all appointments");
            System.out.println(e.getMessage());
        }
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

    public void createOrUpdateAppointment(Appointment appointment) {

        var existingAppointment = this.fetchAppointmentById(appointment.getAppointmentId());

        String sql =
            "INSERT INTO appointments (" +
                "Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, " +
                "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                "ON DUPLICATE KEY UPDATE " +
                "Appointment_ID=VALUES(Appointment_ID), " +
                "Title=VALUES(Title), " +
                "Description=VALUES(Description), " +
                "Location=VALUES(Location)," +
                "Type=VALUES(Type)," +
                "Start=VALUES(Start)," +
                "End=VALUES(End)," +
                "Create_Date=VALUES(Create_Date)," +
                "Created_By=VALUES(Created_By)," +
                "Last_Update=VALUES(Last_Update)," +
                "Last_Updated_By=VALUES(Last_Updated_By)," +
                "Customer_ID=VALUES(Customer_ID)," +
                "User_ID=VALUES(User_ID)," +
                "Contact_ID=VALUES(Contact_ID)";
        try {
            PreparedStatement ps = this.getDb().prepareStatement(sql);
            ps.setObject(1, appointment.getAppointmentId());
            ps.setString(2, appointment.getTitle());
            ps.setString(3, appointment.getDescription());
            ps.setString(4, appointment.getLocation());
            ps.setString(5, appointment.getType());
            ps.setTimestamp(6, appointment.getUtcStartTimestamp());
            ps.setTimestamp(7, appointment.getUtcEndTimestamp());
            ps.setTimestamp(8, existingAppointment.getCreateDate() != null
                ? existingAppointment.getCreateDate()
                : new Timestamp(System.currentTimeMillis())
            );
            ps.setString(9, existingAppointment.getCreatedBy() != null
                ? existingAppointment.getCreatedBy()
                : AuthorizedState.getInstance().getAuthorizedUser().getUserName()
            );
            ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            ps.setString(11, AuthorizedState.getInstance().getAuthorizedUser().getUserName());
            ps.setInt(12, appointment.getCustomerId());
            ps.setInt(13, existingAppointment.getUserId() != null
                ? existingAppointment.getUserId()
                : AuthorizedState.getInstance().getAuthorizedUser().getUserId()
            );
            ps.setInt(14, appointment.getContactId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating or updating appointment " + appointment.getAppointmentId());
            System.out.println(e.getMessage());
        }

        this.fetchAll();
        var reportRepository = new ReportsRepository();
        reportRepository.refreshAllReports();
    }

    public void deleteAppointment(Appointment appointment) {
        try {
            var ps = this.getDb().prepareStatement(
                "DELETE FROM appointments WHERE Appointment_ID = ?"
            );
            ps.setInt(1, appointment.getAppointmentId());
            ps.setMaxRows(1);
            ps.executeUpdate();

            this.fetchAll();
        } catch (SQLException e) {
            System.out.println("Error deleting appointment by id: " + appointment.getAppointmentId());
            System.out.println(e.getMessage());
        }
    }

    public void fetchAppointmentsByRange(ZonedDateTime startView, ZonedDateTime endView) {
        var startTimestamp = Timestamp.valueOf(startView.toLocalDateTime());
        var endTimestamp = Timestamp.valueOf(endView.toLocalDateTime());

        AppointmentList.getInstance().getAppointmentList().clear();

        System.out.println("Grabbing appointments from " + startTimestamp.toString()
            + " - " + endTimestamp.toString());

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT * FROM appointments " +
                    "WHERE Start >= ? AND End <= ?"
            );
            ps.setTimestamp(1, startTimestamp);
            ps.setTimestamp(2, endTimestamp);

            var rs = ps.executeQuery();
            while (rs.next()) {
                AppointmentList.getInstance().getAppointmentList().add(this.fetchRsIntoAppointment(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all appointments");
            System.out.println(e.getMessage());
        }
    }
}


