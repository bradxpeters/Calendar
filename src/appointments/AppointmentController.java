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
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    public void prefill(Appointment app) {
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

        var startHour = appointmentStartHourComboBox.getValue().toString();
        var endHour = appointmentEndHourComboBox.getValue().toString();

        var startMin = appointmentStartMinuteComboBox.getValue().toString();
        var endMin = appointmentEndMinuteComboBox.getValue().toString();

        var beginTime = LocalTime.of(Integer.parseInt(startHour), Integer.parseInt(startMin));
        var endTime = LocalTime.of(Integer.parseInt(endHour), Integer.parseInt(endMin));

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

    private boolean isAppointmentDuringBusinessHours(Appointment appointment) {
        var start = appointment.getStart().withZoneSameInstant(ZoneId.of("America/New_York"));
        var end = appointment.getEnd().withZoneSameInstant(ZoneId.of("America/New_York"));

        var startLimit1 = ZonedDateTime.of(
            start.toLocalDate(), LocalTime.of(7, 59), ZoneId.of("America/New_York"));
        var stopLimit1 = ZonedDateTime.of(
            start.toLocalDate(), LocalTime.of(22, 1), ZoneId.of("America/New_York"));
        var startLimit2 = ZonedDateTime.of(
            end.toLocalDate(), LocalTime.of(7, 59), ZoneId.of("America/New_York"));
        var stopLimit2 = ZonedDateTime.of(
            end.toLocalDate(), LocalTime.of(22, 1), ZoneId.of("America/New_York"));

        var isWeekend = Stream.of(DayOfWeek.SATURDAY, DayOfWeek.SATURDAY)
            .anyMatch(dayOfWeek -> Arrays.asList(start.getDayOfWeek(), end.getDayOfWeek()).contains(dayOfWeek));

        var isValidTime = startLimit1.isBefore(start)
            && stopLimit1.isAfter(start)
            && startLimit2.isBefore(end)
            && stopLimit2.isAfter(end);

        return !isWeekend && isValidTime;
    }

    private boolean isOverlappingAppointment(Appointment appointment) {
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
