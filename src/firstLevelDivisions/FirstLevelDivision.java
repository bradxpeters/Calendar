package firstLevelDivisions;

import java.sql.Timestamp;

/**
 * The type First level division.
 */
public class FirstLevelDivision {

    private Integer divisionId;

    private String division;

    private Timestamp createDate;

    private String createdBy;

    private Timestamp lastUpdate;

    private String lastUpdatedBy;

    private Integer countryId;

    /**
     * Gets division id.
     *
     * @return the division id
     */
    public Integer getDivisionId() {
        return divisionId;
    }

    /**
     * Sets division id.
     *
     * @param divisionId the division id
     */
    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Gets division.
     *
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets division.
     *
     * @param division the division
     */
    public void setDivision(String division) {
        this.division = division;
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
     * Gets country id.
     *
     * @return the country id
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * Sets country id.
     *
     * @param countryId the country id
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return division;
    }
}
