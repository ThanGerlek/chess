package client;

public class SessionData {
    private AuthorizationLevel authLevel;
    private String authTokenString;
    private String username;

    public SessionData() {
        this(AuthorizationLevel.ANY, null, null);
    }

    public SessionData(AuthorizationLevel authLevel, String authTokenString, String username) {
        this.authLevel = authLevel;
        this.authTokenString = authTokenString;
        this.username = username;
    }

    public void clearUserData() {
        setUserData(null, null);
        this.authLevel = AuthorizationLevel.ANY;
    }

    public void setUserData(String authTokenString, String username) {
        this.authTokenString = authTokenString;
        this.username = username;
        if (authTokenString == null || authTokenString.isEmpty()) {
            this.authLevel = AuthorizationLevel.ANY;
        } else {
            this.authLevel = AuthorizationLevel.USER;
        }
    }

    public AuthorizationLevel getAuthLevel() {
        return authLevel;
    }

    public String getAuthTokenString() {
        return authTokenString;
    }

    public String getUsername() {
        return username;
    }
}
