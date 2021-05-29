package appointments;

import contacts.Contact;
import contacts.ContactList;
import contacts.ContactRepository;
import customers.Customer;
import customers.CustomerList;
import customers.CustomerRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The type Appointment controller.
 */
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

    private boolean isUpdatingAppointment = false;
    private AppointmentRepository appointmentRepository;
    private ContactRepository contactRepository;
    private CustomerRepository customerRepository;

    /**
     * Initialize the Appointment Controller
     * Public for JavaDocs
     *
     * I used a Lambda here to generate a list of integers from 0 - 24 in a concise
     * and elegant way. Originally I had a regular for loop which was ugly and overkill for this need.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        var hoursList = FXCollections.observableArrayList(new ArrayList<Integer>());
        IntStream.rangeClosed(0, 24)
            .forEach(hoursList::add);
        var minuteList = FXCollections.observableArrayList(0, 15, 30, 45);

        appointmentStartHourComboBox.setItems(hoursList);
        appointmentEndHourComboBox.setItems(hoursList);

        appointmentStartMinuteComboBox.setItems(minuteList);
        appointmentEndMinuteComboBox.setItems(minuteList);

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

    /**
     * Prefill when updating customer.
     *
     * @param appointment the appointment
     */
    public void prefill(Appointment appointment) {
        isUpdatingAppointment = true;

        appointmentIdTextField.setText(String.valueOf(appointment.getAppointmentId()));
        appointmentTitleTextField.setText(appointment.getTitle());
        appointmentDescriptionTextField.setText(appointment.getDescription());
        appointmentLocationTextField.setText(appointment.getLocation());
        appointmentContactComboBox.setValue(this.contactRepository.fetchContactById(appointment.getContactId()));
        appointmentTypeTextField.setText(appointment.getType());
        appointmentStartDatePicker.setValue(appointment.getStart().toLocalDate());
        appointmentStartHourComboBox.setValue(appointment.getStart().getHour());
        appointmentStartMinuteComboBox.setValue(appointment.getStart().getMinute());
        appointmentEndDatePicker.setValue(appointment.getEnd().toLocalDate());
        appointmentEndHourComboBox.setValue(appointment.getEnd().getHour());
        appointmentEndMinuteComboBox.setValue(appointment.getEnd().getMinute());
        appointmentCustomerComboBox.setValue(this.customerRepository.fetchCustomerById(appointment.getCustomerId()));
    }

    /**
     * Handle appointment submit button.
     * Also performs validations on submission
     *
     * @param event the event
     */
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

        var startHour = appointmentStartHourComboBox.getValue().toString();
        var endHour = appointmentEndHourComboBox.getValue().toString();

        var startMin = appointmentStartMinuteComboBox.getValue().toString();
        var endMin = appointmentEndMinuteComboBox.getValue().toString();

        var startTime = LocalTime.of(Integer.parseInt(startHour), Integer.parseInt(startMin));
        var endTime = LocalTime.of(Integer.parseInt(endHour), Integer.parseInt(endMin));

        var begin = ZonedDateTime.of(
            startDate,
            startTime,
            ZoneId.systemDefault()
        );
        var end = ZonedDateTime.of(
            endDate,
            endTime,
            ZoneId.systemDefault()
        );

        tempAppointment.setStart(begin);
        tempAppointment.setEnd(end);

        if (!isAppointmentDuringBusinessHours(tempAppointment)) {
            var alert = new Alert(Alert.AlertType.ERROR, "Appointment outside of business hours");
            alert.setHeaderText("Error!");
            alert.showAndWait();
            return;
        }

        if (isOverlappingAppointment(tempAppointment)) {
            var alert = new Alert(Alert.AlertType.ERROR, "Appointments are overlapping!");
            alert.setHeaderText("Error!");
            alert.showAndWait();
            return;
        }

        this.appointmentRepository.createOrUpdateAppointment(tempAppointment);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Is appointment during business hours.
     *
     * @param appointment the appointment
     * @return the boolean
     */
    public Boolean isAppointmentDuringBusinessHours(Appointment appointment) {
        var appointmentStartEastern = appointment
            .getStart()
            .withZoneSameInstant(ZoneId.of("America/New_York"));

        var appointmentEndEastern = appointment
            .getEnd()
            .withZoneSameInstant(ZoneId.of("America/New_York"));

        var businessHoursStart = ZonedDateTime
            .of(LocalDate.now().atTime(8,0), ZoneId.of("America/New_York"));

        var businessHoursEnd = ZonedDateTime
            .of(LocalDate.now().atTime(22,0), ZoneId.of("America/New_York"));

        var daysToCheck = Arrays.asList(
            appointmentStartEastern.getDayOfWeek(),
            appointmentEndEastern.getDayOfWeek()
        );

        var isWeekend = Stream.of(DayOfWeek.SATURDAY, DayOfWeek.SATURDAY)
            .anyMatch(daysToCheck::contains);

        var isValidTime = businessHoursStart.getHour() <= appointmentStartEastern.getHour()
            && businessHoursEnd.getHour() >= appointmentEndEastern.getHour();

        if (isWeekend) {
            System.out.println("Weekend check failed");
        }

        if (!isValidTime) {
            System.out.println("Valid Time Check Failed");
            System.out.println("businessHoursStart.getHour() " + businessHoursStart.getHour());
            System.out.println("appointmentStartEastern.getHour() " + appointmentStartEastern.getHour());
        }

        return !isWeekend && isValidTime;
    }

    /**
     * Check for overlapping appointments
     * Public for JavaDocs
     *
     * I use a Lambda here to easily accomplish several tasks to check if an appointment
     * overlaps with any existing appointment. First it filters out out the appointment that
     * is passed into the function. Then it loops through each appointment to see if the times
     * overlap.
     */
    public boolean isOverlappingAppointment(Appointment appointment) {
        var isOverlapping = new AtomicBoolean(false);

        AppointmentList.getInstance()
            .getAppointmentList()
            .stream()
            // Confirm same customer but not exact same appointment
            .filter(a -> !a.getAppointmentId().equals(appointment.getAppointmentId())
                && a.getCustomerId().equals(appointment.getCustomerId()))
            .forEach(a -> {
                    if (a.getStart().isBefore(appointment.getStart())) {
                        if (a.getEnd().isAfter(appointment.getStart())) {
                            isOverlapping.set(true);
                        }
                    } else {
                        if (appointment.getEnd().isAfter(a.getStart())) {
                            isOverlapping.set(true);
                        }
                    }
                }
            );

        return isOverlapping.get();
    }

    /**
     * Sets appointment repository.
     *
     * @param appointmentRepository the appointment repository
     */
    public void setAppointmentRepository(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Sets contact repository.
     *
     * @param contactRepository the contact repository
     */
    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Sets customer repository.
     *
     * @param customerRepository the customer repository
     */
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
