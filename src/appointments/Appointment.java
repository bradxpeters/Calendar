package appointments;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * The type Appointment.
 */
public class Appointment {

    private Integer appointmentId;

    private String title;

    private String description;

    private String location;

    private String type;

    private ZonedDateTime start;

    private ZonedDateTime end;

    private Timestamp createDate;

    private String createdBy;

    private Timestamp lastUpdate;

    private String lastUpdatedBy;

    private Integer customerId;

    private Integer userId;

    private Integer contactId;

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        LocalDateTime localDateTimeNoTimeZone = start.toLocalDateTime();
        this.start = localDateTimeNoTimeZone.atZone(ZoneId.systemDefault());;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        LocalDateTime localDateTimeNoTimeZone = end.toLocalDateTime();
        this.end = localDateTimeNoTimeZone.atZone(ZoneId.systemDefault());;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }
}
