package contacts;

/**
 * The type Contact.
 */
public class Contact {

    private Integer contactId;

    private String contactName;

    private String email;

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

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return contactName;
    }
}
