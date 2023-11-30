package client;

public class AuthorizationRole {
    // GUEST < USER < CONSOLE < SUPERUSER
    // PLAYER and OBSERVER must be exact matches.
    public static AuthorizationRole GUEST = new AuthorizationRole(Role.GUEST);
    public static AuthorizationRole USER = new AuthorizationRole(Role.USER);
    public static AuthorizationRole CONSOLE = new AuthorizationRole(Role.CONSOLE);
    public static AuthorizationRole SUPERUSER = new AuthorizationRole(Role.SUPERUSER);
    public static AuthorizationRole PLAYER = new AuthorizationRole(Role.PLAYER);
    public static AuthorizationRole OBSERVER = new AuthorizationRole(Role.OBSERVER);

    private final Role role;

    private AuthorizationRole(Role role) {
        this.role = role;
    }

    public boolean hasPermission(AuthorizationRole required) {
        switch (required.role) {
            case GUEST -> {
                return role != Role.PLAYER && role != Role.OBSERVER;
            }
            case USER -> {
                return role == Role.GUEST || role == Role.CONSOLE || role == Role.SUPERUSER;
            }
            case CONSOLE -> {
                return role == Role.CONSOLE || role == Role.SUPERUSER;
            }
            case SUPERUSER -> {
                return role == Role.SUPERUSER;
            }
            default -> {
                return role == required.role;
            }
        }
    }

    private enum Role {
        GUEST,
        USER,
        PLAYER,
        OBSERVER,
        CONSOLE,
        SUPERUSER
    }
}
