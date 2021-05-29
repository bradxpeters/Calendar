package reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * The type Report lists.
 */
public class ReportLists {

    private static ReportLists INSTANCE;
    private static ObservableList<AppointmentSummaryReport> appointmentSummaryReportsList;
    private static ObservableList<AppointmentByContactReport> appointmentByContactReportList;
    private static ObservableList<CustomerLocationReport> customerLocationReportList;

    private ReportLists() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ReportLists getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReportLists();
        }

        return INSTANCE;
    }

    /**
     * Gets appointment summary reports list.
     *
     * @return the appointment summary reports list
     */
    public ObservableList<AppointmentSummaryReport> getAppointmentSummaryReportsList() {
        if (appointmentSummaryReportsList == null) {
            appointmentSummaryReportsList = FXCollections.observableList(new ArrayList<>());

            var reportRepository = new ReportsRepository();
            reportRepository.fetchAppointmentSummaryReport();
        }

        return appointmentSummaryReportsList;
    }

    /**
     * Gets appointments by contact list.
     *
     * @return the appointments by contact list
     */
    public ObservableList<AppointmentByContactReport> getAppointmentsByContactList() {
        if (appointmentByContactReportList == null) {
            appointmentByContactReportList = FXCollections.observableList(new ArrayList<>());

            var reportRepository = new ReportsRepository();
            reportRepository.fetchAppointmentsByContactReport();
        }

        return appointmentByContactReportList;
    }

    /**
     * Gets customers by location report.
     *
     * @return the customers by location report
     */
    public ObservableList<CustomerLocationReport> getCustomersByLocationReport() {
        if (customerLocationReportList == null) {
            customerLocationReportList = FXCollections.observableList(new ArrayList<>());

            var reportRepository = new ReportsRepository();
            reportRepository.fetchCustomersByLocation();
        }

        return customerLocationReportList;
    }

}
