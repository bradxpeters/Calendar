package reports;

import appointments.Appointment;

/**
 * The type Appointment by contact report.
 */
public class AppointmentByContactReport extends Appointment {
    private String contactName;

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets contact name.
     *
     * @param contactName the contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
