package client;

public class SessionData {
    private AuthorizationRole authRole;
    private String authTokenString;
    private String username;

    public SessionData() {
        this(AuthorizationRole.GUEST, null, null);
    }

    public SessionData(AuthorizationRole authRole, String authTokenString, String username) {
        this.authRole = authRole;
        this.authTokenString = authTokenString;
        this.username = username;
    }

    public void clearUserData() {
        setUserData(null, null);
        this.authRole = AuthorizationRole.GUEST;
    }

    public void setUserData(String authTokenString, String username) {
        this.authTokenString = authTokenString;
        this.username = username;
        if (authTokenString == null || authTokenString.isEmpty()) {
            this.authRole = AuthorizationRole.GUEST;
        } else {
            this.authRole = AuthorizationRole.USER;
        }
    }

    public AuthorizationRole getAuthRole() {
        return authRole;
    }

    public String getAuthTokenString() {
        return authTokenString;
    }

    public String getUsername() {
        return username;
    }
}
