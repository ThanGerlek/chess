package client;

public class AuthorizationLevel {
    // ANY < USER < CONSOLE < SUPERUSER
    public static AuthorizationLevel ANY = new AuthorizationLevel(0);
    public static AuthorizationLevel USER = new AuthorizationLevel(10);
    public static AuthorizationLevel CONSOLE = new AuthorizationLevel(20);
    public static AuthorizationLevel SUPERUSER = new AuthorizationLevel(30);

    private final int level;

    private AuthorizationLevel(int level) {
        this.level = level;
    }

    public boolean hasPermission(AuthorizationLevel required) {
        return this.level >= required.level;
    }
}
