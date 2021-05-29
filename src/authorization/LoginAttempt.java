package authorization;

import java.sql.Timestamp;

/**
 * The type Login attempt.
 */
public class LoginAttempt {
    private String attemptedUsername;

    private String attemptedPassword;

    private Timestamp timestamp;

    private Integer success;

    /**
     * Gets attempted username.
     *
     * @return the attempted username
     */
    public String getAttemptedUsername() {
        return attemptedUsername;
    }

    /**
     * Sets attempted username.
     *
     * @param attemptedUsername the attempted username
     */
    public void setAttemptedUsername(String attemptedUsername) {
        this.attemptedUsername = attemptedUsername;
    }

    /**
     * Gets attempted password.
     *
     * @return the attempted password
     */
    public String getAttemptedPassword() {
        return attemptedPassword;
    }

    /**
     * Sets attempted password.
     *
     * @param attemptedPassword the attempted password
     */
    public void setAttemptedPassword(String attemptedPassword) {
        this.attemptedPassword = attemptedPassword;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets success.
     *
     * @return the success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * Sets success.
     *
     * @param success the success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }
}
