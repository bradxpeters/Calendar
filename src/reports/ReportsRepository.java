package reports;

import appointments.Appointment;
import appointments.AppointmentList;
import contacts.Contact;
import contacts.ContactList;
import database.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class ReportsRepository {
    private Connection db;

    public ReportsRepository() {
        try {
            this.db = DatabaseConnector.getInstance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void fetchAppointmentSummaryReport() {
        ReportLists.getInstance().getAppointmentSummaryReportsList().clear();

        try {
            var ps = this.getDb().prepareStatement(
                "SELECT Type as type, MONTH(Start) as month, YEAR(Start) as year, count(*) AS count " +
                    "FROM appointments GROUP BY type, month, year"
            );

            var rs = ps.executeQuery();
            while (rs.next()) {
                ReportLists.getInstance().getAppointmentSummaryReportsList().add(this.fetchRsIntoSummaryReport(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching appointment summary report");
            System.out.println(e.getMessage());
        }
    }

    // TODO: LAMBDA COMMENT
    public void fetchAppointmentsByContactReport() {
        ReportLists.getInstance().getAppointmentsByContactList().clear();

        var appointmentsByContact = AppointmentList
            .getInstance()
            .getAppointmentList()
            .stream()
            .map(a -> {
                var report = new AppointmentByContactReport();
                var contact = ContactList.getInstance()
                    .getContactList()
                    .stream()
                    .filter(p -> p.getContactId().equals(a.getContactId()))
                    .findFirst()
                    .orElse(new Contact());

                report.setContactName(contact.getContactName());
                report.setAppointmentId(a.getAppointmentId());
                report.setType(a.getType());
                report.setTitle(a.getTitle());
                report.setDescription(a.getDescription());
                report.setStart(a.getStartSqlTimestamp());
                report.setEnd(a.getEndSqlTimestamp());
                report.setCustomerId(a.getCustomerId());

                return report;
            })
            .collect(Collectors.toList());

        ReportLists.getInstance().getAppointmentsByContactList().addAll(appointmentsByContact);
    }

    private AppointmentSummaryReport fetchRsIntoSummaryReport(ResultSet rs) throws SQLException {
        var report = new AppointmentSummaryReport();

        report.setType(rs.getString("type"));
        report.setMonth(rs.getString("month"));
        report.setYear(rs.getString("year"));
        report.setCount(rs.getInt("count"));

        return report;
    }

    public Connection getDb() {
        return db;
    }
}


