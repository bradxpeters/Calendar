package reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ReportLists {

    private static ReportLists INSTANCE;
    private static ObservableList<AppointmentSummaryReport> appointmentSummaryReportsList;
    private static ObservableList<AppointmentByContactReport> appointmentByContactReportList;
    private static ObservableList<CustomerLocationReport> customerLocationReportList;

    private ReportLists() {
    }

    public static ReportLists getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReportLists();
        }

        return INSTANCE;
    }

    public ObservableList<AppointmentSummaryReport> getAppointmentSummaryReportsList() {
        if (appointmentSummaryReportsList == null) {
            appointmentSummaryReportsList = FXCollections.observableList(new ArrayList<>());

            var reportRepository = new ReportsRepository();
            reportRepository.fetchAppointmentSummaryReport();
        }

        return appointmentSummaryReportsList;
    }

    public ObservableList<AppointmentByContactReport> getAppointmentsByContactList() {
        if (appointmentByContactReportList == null) {
            appointmentByContactReportList = FXCollections.observableList(new ArrayList<>());

            var reportRepository = new ReportsRepository();
            reportRepository.fetchAppointmentsByContactReport();
        }

        return appointmentByContactReportList;
    }

    public ObservableList<CustomerLocationReport> getCustomersByLocationReport() {
        if (customerLocationReportList == null) {
            customerLocationReportList = FXCollections.observableList(new ArrayList<>());

            var reportRepository = new ReportsRepository();
            reportRepository.fetchCustomersByLocation();
        }

        return customerLocationReportList;
    }

}
