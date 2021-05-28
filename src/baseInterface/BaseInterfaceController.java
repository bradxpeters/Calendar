package baseInterface;

import appointments.Appointment;
import appointments.AppointmentController;
import appointments.AppointmentRepository;
import authorization.AuthorizedState;
import contacts.Contact;
import countries.Country;
import customers.AddCustomerFormController;
import customers.Customer;
import customers.CustomerList;
import customers.CustomerRepository;
import firstLevelDivisions.FirstLevelDivision;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class BaseInterfaceController implements Initializable {

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
    private ToggleGroup ViewPeriod;

    @FXML
    private RadioButton weekRadio;

    @FXML
    private RadioButton monthRadio;

    @FXML
    private Label viewLabel;

    @FXML
    private Label NextAppointment;

    @FXML
    private Button AppointmentAddButton;

    @FXML
    private Button AppointmentUpdateButton;

    @FXML
    private Button AppointmentDeleteButton;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Button customerAddButton;

    @FXML
    private Button customerUpdateButton;

    @FXML
    private Button customerDeleteButton;

    public static ZonedDateTime startView;

    public static ZonedDateTime endView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.setUpTableColumns();
        this.populateInitialData();

        //Update all lists
//            System.out.println("Updating Country List...");
//            mysql.database.updateCountriesList();
//            System.out.println("Done!\nUpdating Division List...");
//            mysql.database.updateDivisionsList();//Requires Countries
//            System.out.println("Done!\nUpdating Contact List...");
//            mysql.database.updateContactsList();
//            System.out.println("Done!\nUpdating Customer List...");
//            mysql.database.updateCustomerList();//Requires Divisions
//            System.out.println("Done!\nUpdating Appointment List...");
//            mysql.database.updateAppointmentList();// Requires Contacts and Customers
//            System.out.println("Done!");
//
//            System.out.println("Checking for appointments within 15 minutes... ");
//            startView = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
//            //startView.minusDays(0);
//            endView = startView.plusMinutes(15);
//            String begin = startView.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            String end = endView.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            mysql.database.updateAppointmentListByStart(begin, end);
//            System.out.println("Done! Found " + Appointments.appointmentList.size() + " Appointments");

//        if(Appointments.appointmentList.size() > 0) NextAppointment.setText("Id: "+ Appointments.appointmentList.get(0).getApointmentID() + " Appointment: " + Appointments.appointmentList.get(0).getTitle() + " Starts at: " + Appointments.appointmentList.get(0).getStartTime());
//        else NextAppointment.setText("There are no appointments soon.");
//
//        //Fire teh week filter to prefil the appointments list and preselect filter
//        weekRadio.fire();
    }

    private void populateInitialData() {
        // Populate customer table
        var customerRepository = new CustomerRepository();
        customerTableView.setItems(CustomerList.getInstance().getCustomerList());
        customerTableView.getSortOrder().add(customerIdColumn);

        // Populate appointment table
        var appointmentRepository = new AppointmentRepository();
        appointmentTableView.setItems(appointmentRepository.fetchAll());
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
    }

    //When add customer button is presssed cutomer menu opens to add new customer to database
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
        }
        catch(IOException e) {
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
            stage.setTitle("Add Or Update Customer");
            stage.setScene(scene);
            stage.show();

            AppointmentController controller = loader.getController();
            if (selectedAppointment != null && selectedButtonText.equals("Update")) {
                controller.prefill(selectedAppointment);
            }
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * When the appointment delete button is pressed this function will run
     * <p>This function will pull the appointment that is selected and make sure an appointment
     * was selected. It will start a new thread to confirm if the user wants to delete the appointment with
     * a popup. Once the popup was confirmed then it will pass the selected appointment to
     * mysql.deleteAppointment() to remove it from the database.
     */
    @FXML
    private void appointmentDelete(ActionEvent event) {
//        Appointment selected = appointmentTableView.getSelectionModel().getSelectedItem();
//
//        if(selected == null) return;
//
//        //AppDeleteRun ADRun = new AppDeleteRun(selected);
//        //new Thread(ADRun).start();
//
//        Runnable ADRun =
//            () -> { if (!ConfirmAppointmentDelete(selected)) return;
//
//                try {
//                    mysql.database.deleteAppointment(selected);
//                }
//                catch (SQLException e) {
//                    System.out.println("SQL ERROR!!!" + e);
//                } };
//        Thread thread = new Thread(ADRun);
//        thread.start();
    }

    //On radio button change update the timeline
    @FXML
    private void viewRadioChange(ActionEvent event) {
//        //If week radio button
//        if(ViewPeriod.getSelectedToggle().equals(weekRadio)){
//            System.out.println("Week is selected");
//            startView = ZonedDateTime.now().withHour(0).withMinute(0);
//            endView = startView.plusWeeks(1).withHour(23).withMinute(59);
//        }
//        //if month radio button
//        if(ViewPeriod.getSelectedToggle().equals(monthRadio)){
//            System.out.println("Month is selected");
//            startView = ZonedDateTime.now().withHour(0).withMinute(0);
//            endView = startView.plusMonths(1).withHour(23).withMinute(59);
//        }
//        updateView();
    }

    //get a new list to diplay base on the begin and end dates
    private void updateView() {
//        String begin = startView.format(DateTimeFormatter.ISO_LOCAL_DATE);
//        String end = endView.format(DateTimeFormatter.ISO_LOCAL_DATE);
//        viewLabel.setText(begin + " :: " + end);
//
//        begin = startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
//        end = endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
//
//        try {
//            mysql.database.updateAppointmentList(begin, end);
//        }
//        catch(SQLException e) {
//            System.out.println("SQL Error!!! " + e);
//        }
    }

    //when the view minus button is pressed subtract a week or month based on radio buttons
    @FXML
    private void ViewMinus(ActionEvent event) {
//        if(ViewPeriod.getSelectedToggle().equals(weekRadio)){
//            startView = startView.minusWeeks(1);
//            endView = startView.plusWeeks(1);
//        }
//
//        if(ViewPeriod.getSelectedToggle().equals(monthRadio)){
//            startView = startView.minusMonths(1);
//            endView = startView.plusMonths(1);
//        }
//        updateView();
    }

    //when the view plus button is pressed add a week or month based on radio button
    @FXML
    private void ViewPlus(ActionEvent event) {
//        if(ViewPeriod.getSelectedToggle().equals(weekRadio)){
//            startView = startView.plusWeeks(1);
//            endView = startView.plusWeeks(1);
//        }
//
//        if(ViewPeriod.getSelectedToggle().equals(monthRadio)){
//            startView = startView.plusMonths(1);
//            endView = startView.plusMonths(1);
//        }
//        updateView();
    }

    /**
     * used by the cusDeleteRun class to confirm deletion with a popup
     * @param selected the customer object to be deleted
     * @return true if popup was confirmed
     */
    private boolean ConfirmCustomerDelete(Customer selected) {
//        int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selected.getName() + "?", "Delete Customer?", JOptionPane.YES_NO_OPTION);
//
//        return i == 0;
        return true;
    }

    /**
     * used by the appDeleteRun class to confirm deletion with a popup
     * @param selected the customer object to be deleted
     * @return true if popup was confirmed
     */
    private boolean ConfirmAppointmentDelete(Appointment selected){
//        int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selected.getApointmentID() + ": " + selected.getType() + "?", "Delete Appointment?", JOptionPane.YES_NO_OPTION);
//
//        return i == 0;
        return true;
    }

    // run the first report action button
    @FXML
    private void RunReport1(ActionEvent event) {
//        new Reports().Report(1);
    }

    // run the second report action button
    @FXML
    private void RunReport2(ActionEvent event) {
//        new Reports().Report(2);
    }

    //runt he third report action button
    @FXML
    private void RunReport3(ActionEvent event) {
//        new Reports().Report(3);
    }
}
