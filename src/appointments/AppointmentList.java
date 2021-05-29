package appointments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * The type Appointment list.
 */
public class AppointmentList {
    private static AppointmentList INSTANCE;
    private static ObservableList<Appointment> allAppointments;

    private AppointmentList() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AppointmentList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppointmentList();
        }

        return INSTANCE;
    }

    /**
     * Gets appointment list.
     *
     * @return the appointment list
     */
    public ObservableList<Appointment> getAppointmentList() {
        if (allAppointments == null) {
            allAppointments = FXCollections.observableList(new ArrayList<>());

            var appointmentRepository = new AppointmentRepository();
            appointmentRepository.fetchAll();
        }

        return allAppointments;
    }

    /**
     * Sets all customers.
     *
     * @param appointments the appointments
     */
    public void setAllCustomers(ObservableList<Appointment> appointments) {
        allAppointments = appointments;
    }
}
