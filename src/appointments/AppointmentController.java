package appointments;

import contacts.Contact;
import contacts.ContactList;
import contacts.ContactRepository;
import customers.Customer;
import customers.CustomerList;
import customers.CustomerRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {
    @FXML
    private TextField appointmentIdTextField;

    @FXML
    private TextField appointmentTitleTextField;

    @FXML
    private TextField appointmentDescriptionTextField;

    @FXML
    private TextField appointmentLocationTextField;

    @FXML
    private ComboBox<Contact> appointmentContactComboBox;

    @FXML
    private TextField appointmentTypeTextField;

    @FXML
    private DatePicker appointmentStartDatePicker;

    @FXML
    private ComboBox<Integer> appointmentStartHourComboBox;

    @FXML
    private ComboBox<Integer> appointmentStartMinuteComboBox;

    @FXML
    private DatePicker appointmentEndDatePicker;

    @FXML
    private ComboBox<Integer> appointmentEndHourComboBox;

    @FXML
    private ComboBox<Integer> appointmentEndMinuteComboBox;

    @FXML
    private ComboBox<Customer> appointmentCustomerComboBox;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private boolean isUpdatingAppointment = false;

    private AppointmentRepository appointmentRepository;

    private ContactRepository contactRepository;

    private CustomerRepository customerRepository;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Make an observable list with 0-23 hour options
        ObservableList<Integer> hr = FXCollections.observableArrayList();
        int i = 0;
        while(i < 24) {
            hr.add(i);
            i++;
        }

        //fill hour combo boxes with hr list
        appointmentStartHourComboBox.setItems(hr);
        appointmentEndHourComboBox.setItems(hr);

        //make list with 0, 15, 30, 45
        ObservableList<Integer> min = FXCollections.observableArrayList(0, 15, 30, 45);

        //fill min combo boxes with min list
        appointmentStartMinuteComboBox.setItems(min);
        appointmentEndMinuteComboBox.setItems(min);

        //Clear the error at top of screen
        ClearError(new ActionEvent());

        this.setAppointmentRepository(new AppointmentRepository());
        this.setContactRepository(new ContactRepository());
        this.setCustomerRepository(new CustomerRepository());

        this.setupContactComboBox();
        this.setupAppointmentCustomerComboBox();
    }

    private void setupContactComboBox() {
        Callback<ListView<Contact>, ListCell<Contact>> factory = lv -> new ListCell<>() {

            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getContactName());
            }

        };

        appointmentContactComboBox.setItems(ContactList.getInstance().getContactList());
        appointmentContactComboBox.setCellFactory(factory);
        appointmentContactComboBox.setButtonCell(factory.call(null));
    }

    private void setupAppointmentCustomerComboBox() {
        Callback<ListView<Customer>, ListCell<Customer>> factory = lv -> new ListCell<>() {

            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getCustomerName());
            }

        };

        appointmentCustomerComboBox.setItems(CustomerList.getInstance().getCustomerList());
        appointmentCustomerComboBox.setCellFactory(factory);
        appointmentCustomerComboBox.setButtonCell(factory.call(null));
    }

    public void prefill (Appointment app) {
        isUpdatingAppointment = true;

        appointmentIdTextField.setText(String.valueOf(app.getAppointmentId()));
        appointmentTitleTextField.setText(app.getTitle());
        appointmentDescriptionTextField.setText(app.getDescription());
        appointmentLocationTextField.setText(app.getLocation());
        appointmentContactComboBox.setValue(this.contactRepository.fetchContactById(app.getContactId()));
        appointmentTypeTextField.setText(app.getType());
        appointmentStartDatePicker.setValue(app.getStart().toLocalDate());
        appointmentStartHourComboBox.setValue(app.getStart().getHour());
        appointmentStartMinuteComboBox.setValue(app.getStart().getMinute());
        appointmentEndDatePicker.setValue(app.getEnd().toLocalDate());
        appointmentEndHourComboBox.setValue(app.getEnd().getHour());
        appointmentEndMinuteComboBox.setValue(app.getEnd().getMinute());
        appointmentCustomerComboBox.setValue(this.customerRepository.fetchCustomerById(app.getCustomerId()));
    }

    @FXML
    private void handleAppointmentSubmitButton(ActionEvent event) {

        System.out.print("Appointment creation/updating started");
        var tempAppointment = new Appointment();
        tempAppointment.setAppointmentId(isUpdatingAppointment
            ? Integer.parseInt(appointmentIdTextField.getText())
            : null);
        tempAppointment.setTitle(appointmentTitleTextField.getText());
        tempAppointment.setDescription(appointmentDescriptionTextField.getText());
        tempAppointment.setLocation(appointmentLocationTextField.getText());
        tempAppointment.setContactId(appointmentContactComboBox.getValue().getContactId());
        tempAppointment.setType(appointmentTypeTextField.getText());
        tempAppointment.setCustomerId(appointmentCustomerComboBox.getValue().getCustomerId());

        var startDate = appointmentStartDatePicker.getValue();
        var endDate = appointmentEndDatePicker.getValue();

        var startHr = appointmentStartHourComboBox.getValue().toString();
        var endHr = appointmentEndHourComboBox.getValue().toString();

        var startMin = appointmentStartMinuteComboBox.getValue().toString();
        var endMin = appointmentEndMinuteComboBox.getValue().toString();

        var beginTime = LocalTime.of(Integer.parseInt(startHr), Integer.parseInt(startMin));
        var endTime = LocalTime.of(Integer.parseInt(endHr), Integer.parseInt(endMin));

        var begin = ZonedDateTime.of(
            startDate,
            beginTime,
            ZoneId.systemDefault()
        );
        var end = ZonedDateTime.of(
            endDate,
            endTime,
            ZoneId.systemDefault()
        );

        //.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        tempAppointment.setStart(begin);
        tempAppointment.setEnd(end);

//        if(!isBussinessHours(app)){
//            errorMessageLabel.setText("Selected Times are outside businness hours");
//            return;
//        }
//
//        //We need to check if the new appointment will overlap any existing appointment
//        if(isOverlapping(app)){
//            errorMessageLabel.setText("Selected Times are overlapping another appointment");
//            return;
//        }

        this.appointmentRepository.createOrUpdateAppointment(tempAppointment);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    //Runs when the cancel button is pressed and closed the window
    @FXML
    private void AppCancel(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Used to check if a new appointment is in business hours in the appointment menu
     * before sending it to the database
     * @param app the appointment to be checked
     * @return true if it is in business hours
     */
    private boolean isBussinessHours(Appointment app){
//        ZonedDateTime start = app.getStartTimeObj().withZoneSameInstant(ZoneId.of("America/New_York"));
//        ZonedDateTime end = app.getEndTimeObj().withZoneSameInstant(ZoneId.of("America/New_York"));
//
//        ZonedDateTime startLimit1 = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(7, 59), ZoneId.of("America/New_York"));
//        ZonedDateTime stopLimit1 = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(22, 1), ZoneId.of("America/New_York"));
//        ZonedDateTime startLimit2 = ZonedDateTime.of(end.toLocalDate(), LocalTime.of(7, 59), ZoneId.of("America/New_York"));
//        ZonedDateTime stopLimit2 = ZonedDateTime.of(end.toLocalDate(), LocalTime.of(22, 1), ZoneId.of("America/New_York"));
//
//        if(start.getDayOfWeek() == DayOfWeek.SATURDAY) return false;
//        if(start.getDayOfWeek() == DayOfWeek.SUNDAY) return false;
//        if(end.getDayOfWeek() == DayOfWeek.SATURDAY) return false;
//        if(start.getDayOfWeek() == DayOfWeek.SUNDAY) return false;
//        return startLimit1.isBefore(start) && stopLimit1.isAfter(start) && startLimit2.isBefore(end) && stopLimit2.isAfter(end);
        return true;
    }

    boolean overlapCheck = false;

    /**
     * Check if the new appointment will overlap existing appointments after hitting
     * the submit button on the appointment menu but before sending it to the database
     * <p>This is used in Appointment_MenuController.AppSubmit. After the user hit submit on a new
     * or modifying appointment the generated appointment will be passed to isOverlapping to see if the
     * Appointment overlaps with any other appointment
     * @param app the appointment object to be checked
     * @return true if overlapping false if appointment is clear
     */
    private boolean isOverlapping(Appointment app){
//        ZonedDateTime start = app.getStartTimeObj();
//        ZonedDateTime end = app.getEndTimeObj();
//        System.out.println("Starting OverLap check");
//        overlapCheck = false;
//        String startDate = start.withZoneSameInstant(ZoneId.of("Z")).withHour(0).withMinute(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss" ));
//        String endDate = end.withZoneSameInstant(ZoneId.of("Z")).withHour(23).withMinute(59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss" ));
//        ObservableList<Appointments> List = FXCollections.observableArrayList();
//
//        try{
//            List = mysql.database.getAppointmentList(startDate, endDate);
//        }
//        catch(SQLException e){
//            System.out.println("Database Error!!! " + e);
//        }
//
//        List.forEach(appList -> {
//            if(appList.getApointmentID() != app.getApointmentID() ){
//                if(appList.getStartTimeObj().isBefore(start)){
//                    if(appList.getEndTimeObj().isAfter(start)){
//                        overlapCheck = true;
//                        System.out.println("OverLap Check Failed!!!");
//                    }
//                }
//                else{
//                    if(end.isAfter(appList.getStartTimeObj())){
//                        overlapCheck = true;
//                        System.out.println("OverLap Check Failed!!!");
//                    }
//                }
//            }
//        });
//
//        System.out.println("Overlap check done " + overlapCheck);
//        return overlapCheck;
        return false;
    }

    @FXML
    private void ClearError(ActionEvent event) {
        errorMessageLabel.setText("");
    }

    public void setAppointmentRepository(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
