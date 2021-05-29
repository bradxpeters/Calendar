package authorization;

import users.User;

/**
 * The type Authorized state. Holds the currently logged in user.
 */
public class AuthorizedState {
    private static AuthorizedState INSTANCE;
    private static User authorizedUser;

    private AuthorizedState() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AuthorizedState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthorizedState();
        }

        return INSTANCE;
    }

    /**
     * Gets authorized user.
     *
     * @return the authorized user
     */
    public User getAuthorizedUser() {
        return authorizedUser;
    }

    /**
     * Sets authorized user.
     *
     * @param user the user
     */
    public void setAuthorizedUser(User user) {
        authorizedUser = user;
    }
}
