package reports;

/**
 * The type Appointment summary report.
 */
public class AppointmentSummaryReport {
    private String type;

    private String month;

    private String year;

    private Integer count;

    /**
     * Instantiates a new Appointment summary report.
     */
    public AppointmentSummaryReport() {
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
     * Gets month.
     *
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets month.
     *
     * @param month the month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets year.
     *
     * @param year the year
     */
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "AppointmentSummaryReport{" +
            "type='" + type + '\'' +
            ", month='" + month + '\'' +
            ", year='" + year + '\'' +
            ", count=" + count +
            '}';
    }
}
