package authorization;

import java.sql.Timestamp;

public class LoginAttempt {
    private String attemptedUsername;

    private String attemptedPassword;

    private Timestamp timestamp;

    private Integer success;

    public String getAttemptedUsername() {
        return attemptedUsername;
    }

    public void setAttemptedUsername(String attemptedUsername) {
        this.attemptedUsername = attemptedUsername;
    }

    public String getAttemptedPassword() {
        return attemptedPassword;
    }

    public void setAttemptedPassword(String attemptedPassword) {
        this.attemptedPassword = attemptedPassword;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
