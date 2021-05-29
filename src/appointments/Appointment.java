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

    /**
     * Gets appointment id.
     *
     * @return the appointment id
     */
    public Integer getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets appointment id.
     *
     * @param appointmentId the appointment id
     */
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets start.
     *
     * @return the start
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(Timestamp start) {
        LocalDateTime localDateTimeNoTimeZone = start.toLocalDateTime();
        this.start = localDateTimeNoTimeZone.atZone(ZoneId.systemDefault());
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(Timestamp end) {
        LocalDateTime localDateTimeNoTimeZone = end.toLocalDateTime();
        this.end = localDateTimeNoTimeZone.atZone(ZoneId.systemDefault());
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets created by.
     *
     * @return the created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets created by.
     *
     * @param createdBy the created by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets last update.
     *
     * @return the last update
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets last update.
     *
     * @param lastUpdate the last update
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Gets last updated by.
     *
     * @return the last updated by
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets last updated by.
     *
     * @param lastUpdatedBy the last updated by
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Sets customer id.
     *
     * @param customerId the customer id
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Gets contact id.
     *
     * @return the contact id
     */
    public Integer getContactId() {
        return contactId;
    }

    /**
     * Sets contact id.
     *
     * @param contactId the contact id
     */
    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets start sql timestamp.
     *
     * @return the start sql timestamp
     */
    public Timestamp getStartSqlTimestamp() {
        return Timestamp.valueOf(this.start.toLocalDateTime());
    }

    /**
     * Gets end sql timestamp.
     *
     * @return the end sql timestamp
     */
    public Timestamp getEndSqlTimestamp() {
        return Timestamp.valueOf(this.end.toLocalDateTime());
    }

    /**
     * Gets utc start timestamp.
     *
     * @return the utc start timestamp
     */
    public Timestamp getUtcStartTimestamp() {
        return Timestamp.valueOf(this.start.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
    }

    /**
     * Gets utc end timestamp.
     *
     * @return the utc end timestamp
     */
    public Timestamp getUtcEndTimestamp() {
        return Timestamp.valueOf(this.end.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
    }

    @Override
    public String toString() {
        return "Appointment{" +
            "appointmentId=" + appointmentId +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", location='" + location + '\'' +
            ", type='" + type + '\'' +
            ", start=" + start +
            ", end=" + end +
            ", createDate=" + createDate +
            ", createdBy='" + createdBy + '\'' +
            ", lastUpdate=" + lastUpdate +
            ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
            ", customerId=" + customerId +
            ", userId=" + userId +
            ", contactId=" + contactId +
            '}';
    }
}
