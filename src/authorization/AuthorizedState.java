package authorization;

import users.User;

public class AuthorizedState {
    private static AuthorizedState INSTANCE;
    private static User authorizedUser;

    private AuthorizedState() {
    }

    public static AuthorizedState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthorizedState();
        }

        return INSTANCE;
    }

    public User getAuthorizedUser() {
        return authorizedUser;
    }

    public void setAuthorizedUser(User user) {
        authorizedUser = user;
    }
}
