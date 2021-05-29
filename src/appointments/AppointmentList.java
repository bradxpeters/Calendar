package appointments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class AppointmentList {
    private static AppointmentList INSTANCE;
    private static ObservableList<Appointment> allAppointments;

    private AppointmentList() {
    }

    public static AppointmentList getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppointmentList();
        }

        return INSTANCE;
    }

    public ObservableList<Appointment> getAppointmentList() {
        if (allAppointments == null) {
            allAppointments = FXCollections.observableList(new ArrayList<>());

            var appointmentRepository = new AppointmentRepository();
            appointmentRepository.fetchAll();
        }

        return allAppointments;
    }

    public void setAllCustomers(ObservableList<Appointment> appointments) {
        allAppointments = appointments;
    }
}
