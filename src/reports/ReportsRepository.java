package reports;

import database.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            System.out.println("Error fetching all customers");
            System.out.println(e.getMessage());
        }
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


