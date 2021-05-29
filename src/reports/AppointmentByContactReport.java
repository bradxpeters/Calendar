package reports;

import appointments.Appointment;

public class AppointmentByContactReport extends Appointment {
    private String contactName;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
