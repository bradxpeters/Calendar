package baseInterface;

import appointments.Appointment;
import appointments.AppointmentController;
import appointments.AppointmentList;
import appointments.AppointmentRepository;
import contacts.Contact;
import countries.Country;
import customers.AddCustomerFormController;
import customers.Customer;
import customers.CustomerList;
import customers.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reports.AppointmentByContactReport;
import reports.AppointmentSummaryReport;
import reports.CustomerLocationReport;
import reports.ReportLists;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BaseInterfaceController implements Initializable {

    public static ZonedDateTime startView;
    public static ZonedDateTime endView;
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    @FXML
    private TableColumn<Customer, Customer> customerDivisionIdColumn;
    @FXML
    private TableColumn<Customer, Customer> customerDivisionNameColumn;
    @FXML
    private TableColumn<Customer, Country> customerCountryColumn;
    @FXML
    private TableColumn<Customer, String> customerPostalCodeColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDescColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentLocationColumn;
    @FXML
    private TableColumn<Appointment, Contact> appointmentContactColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentStartColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentEndColumn;
    @FXML
    private TableColumn<Appointment, Customer> appointmentCustomerColumn;
    @FXML
    private TableView<AppointmentSummaryReport> appointmentReportTableView;
    @FXML
    private TableColumn<Appointment, String> appointmentReportTypeColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentReportMonthColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentReportYearColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentReportCountColumn;
    @FXML
    private TableView<AppointmentByContactReport> appointmentByContactReportTableView;
    @FXML
    private TableColumn<AppointmentByContactReport, String> appointmentByContactReportContactNameColumn;
    @FXML
    private TableColumn<AppointmentByContactReport, String> appointmentByContactReportIdColumn;
    @FXML
    private TableColumn<AppointmentByContactReport, String> appointmentByContactReportTitleColumn;
    @FXML
    private TableColumn<AppointmentByContactReport, String> appointmentByContactReportTypeColumn;
    @FXML
    private TableColumn<AppointmentByContactReport, String> appointmentByContactReportDescColumn;
    @FXML
    private TableColumn<AppointmentByContactReport, String> appointmentByContactReportStartColumn;
    @FXML
    private TableColumn<AppointmentByContactReport, String> appointmentByContactReportEndColumn;
    @FXML
    private TableColumn<AppointmentByContactReport, String> appointmentByContactReportCustomerIdColumn;
    @FXML
    private TableView<CustomerLocationReport> customerLocationReportTableView ;
    @FXML
    private TableColumn<CustomerLocationReport, String> customerLocationPostalCodeColumn;
    @FXML
    private TableColumn<CustomerLocationReport, Integer> customerLocationCountColumn;


    @FXML
    private ToggleGroup selectedPeriodToggleGroup;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private RadioButton allRadioButton;

    @FXML
    private Label nextAppointmentLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.setUpTableColumns();
        this.populateInitialData();
        this.handleUpcomingAppointmentCheck();
    }

    private void handleUpcomingAppointmentCheck() {
        nextAppointmentLabel.setText("No appointments within the next 15 minutes.");
        var upcomingAppointment = AppointmentList.getInstance().getAppointmentList()
            .stream()
            .filter(a -> a.getStart().isBefore(ZonedDateTime.now().plusMinutes(15)))
            .limit(1)
            .collect(Collectors.toList()).get(0);

        if (upcomingAppointment != null) {
            var alert = new Alert(Alert.AlertType.INFORMATION, "You have an appointment soon! ID: "
                + upcomingAppointment.getAppointmentId() + " " + upcomingAppointment.getStart().toString());
            alert.setHeaderText("Confirmation");
            alert.showAndWait();
            nextAppointmentLabel.setText("Upcoming appointment: " +
                upcomingAppointment.getAppointmentId() + " " +
                upcomingAppointment.getStart().toString()
            );
        }
    }

    private void populateInitialData() {
        // Populate customer table
        customerTableView.setItems(CustomerList.getInstance().getCustomerList());
        customerTableView.getSortOrder().add(customerIdColumn);

        // Populate appointment table
        appointmentTableView.setItems(AppointmentList.getInstance().getAppointmentList());

        // Populate reports tables
        appointmentReportTableView.setItems(ReportLists.getInstance().getAppointmentSummaryReportsList());
        appointmentByContactReportTableView.setItems(ReportLists.getInstance().getAppointmentsByContactList());
        customerLocationReportTableView.setItems(ReportLists.getInstance().getCustomersByLocationReport());

    }

    private void setUpTableColumns() {
        customerIdColumn.setSortType(TableColumn.SortType.ASCENDING);
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerDivisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
        customerDivisionNameColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

        appointmentReportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentReportMonthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        appointmentReportYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        appointmentReportCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        appointmentByContactReportContactNameColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));;
        appointmentByContactReportIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentByContactReportTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentByContactReportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentByContactReportDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentByContactReportStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentByContactReportEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentByContactReportCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        customerLocationPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerLocationCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }

    @FXML
    private void handleAddOrUpdateCustomerButton(ActionEvent event) {
        var selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        var selectedButtonText = ((Button) event.getSource()).getText();

        try {
            FXMLLoader loader = new FXMLLoader(Customer.class.getResource("addCustomerForm.fxml"));
            Parent root = loader.load();
            var scene = new Scene(root);
            var stage = new Stage();
            stage.setTitle("Add Or Update Customer");
            stage.setScene(scene);
            stage.show();

            AddCustomerFormController controller = loader.getController();
            if (selectedCustomer != null && selectedButtonText.equals("Update")) {
                controller.prefill(selectedCustomer);
            }
        } catch (IOException e) {
            System.out.println("Error during launching add customer form.");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleCustomerDeleteButton(ActionEvent event) {
        var selected = customerTableView.getSelectionModel().getSelectedItem();
        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete "
            + selected.getCustomerName() + "?");
        alert.setHeaderText("Confirmation");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            var customerRepository = new CustomerRepository();
            customerRepository.deleteCustomer(selected);
        }
    }

    @FXML
    private void handleAddOrUpdateAppointmentButton(ActionEvent event) {
        var selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        var selectedButtonText = ((Button) event.getSource()).getText();
        try {
            FXMLLoader loader = new FXMLLoader(Appointment.class.getResource("addAppointmentForm.fxml"));
            Parent root = loader.load();
            var scene = new Scene(root);
            var stage = new Stage();
            stage.setTitle("Add Or Update Appointment");
            stage.setScene(scene);
            stage.show();

            AppointmentController controller = loader.getController();
            if (selectedAppointment != null && selectedButtonText.equals("Update")) {
                controller.prefill(selectedAppointment);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
        var selected = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        var alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete appointment "
            + selected.getAppointmentId() + " - " + selected.getType() + "?");
        alert.setHeaderText("Confirmation");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            var appointmentRepository = new AppointmentRepository();
            appointmentRepository.deleteAppointment(selected);
        }
    }

    @FXML
    private void viewRadioChange(ActionEvent event) {
        if (selectedPeriodToggleGroup.getSelectedToggle().equals(weekRadioButton)) {
            System.out.println("Switching to week view");
            startView = ZonedDateTime.now().withHour(0).withMinute(0);
            endView = startView.plusWeeks(1).withHour(23).withMinute(59);
        }
        if (selectedPeriodToggleGroup.getSelectedToggle().equals(monthRadioButton)) {
            System.out.println("Switching to month view");
            startView = ZonedDateTime.now().withHour(0).withMinute(0);
            endView = startView.plusMonths(1).withHour(23).withMinute(59);
        }
        if (selectedPeriodToggleGroup.getSelectedToggle().equals(allRadioButton)) {
            System.out.println("Switching to view all");
            startView = ZonedDateTime.now().withHour(0).withMinute(0);
            endView = startView.plusYears(99).withHour(23).withMinute(59);
        }

        this.updateSelectedPeriodView();
    }

    private void updateSelectedPeriodView() {
        var appointmentRepository = new AppointmentRepository();
        appointmentRepository.fetchAppointmentsByRange(startView, endView);
    }
}
