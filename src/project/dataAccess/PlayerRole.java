package dataAccess;

public class PlayerRole {
    private static final String WHITE_STRING = "white";
    public static final PlayerRole WHITE_PLAYER = new PlayerRole(WHITE_STRING);
    private static final String BLACK_STRING = "black";
    public static final PlayerRole BLACK_PLAYER = new PlayerRole(BLACK_STRING);
    private static final String SPECTATOR_STRING = "spectator";
    public static final PlayerRole SPECTATOR = new PlayerRole(SPECTATOR_STRING);
    private final String roleString;

    private PlayerRole(String roleString) {
        this.roleString = roleString;
    }

    public static String roleToString(PlayerRole role) {
        if (role == null) {
            return SPECTATOR_STRING;
        } else {
            return role.roleString;
        }
    }

    public static PlayerRole stringToRole(String roleString) {
        if (roleString == null || roleString.isEmpty() || SPECTATOR_STRING.equals(roleString)) {
            return SPECTATOR;
        } else if (WHITE_STRING.equals(roleString)) {
            return WHITE_PLAYER;
        } else if (BLACK_STRING.equals(roleString)) {
            return BLACK_PLAYER;
        } else {
            String msg = String.format("Called stringToRole() with an unrecognized role string: '%s'", roleString);
            throw new IllegalArgumentException(msg);
        }
    }

    public String toString() {
        return "PlayerRole." + this.roleString;
    }
}
