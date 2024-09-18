package ui;

import client.AuthorizationRole;

public enum Command {
    // ANY or higher
    HELP("help", "Print a list of available commands", AuthorizationRole.ANY),
    QUIT("quit", "Quit the game", AuthorizationRole.ANY),
    TEST("uuddababs", "SECRETSES!!!", AuthorizationRole.ANY),

    // GUEST
    REGISTER("register", "Register a new user account", AuthorizationRole.GUEST),
    LOGIN("login", "Log in an existing user", AuthorizationRole.GUEST),

    // USER or higher
    LOGOUT("logout", "Log out the current user", AuthorizationRole.USER),
    CREATE_GAME("create", "Create a new chess game", AuthorizationRole.USER),
    LIST_GAMES("list", "List all games", AuthorizationRole.USER),
    JOIN_GAME("join", "Join an existing game", AuthorizationRole.USER),
    OBSERVE_GAME("observe", "Observe an existing game as a spectator", AuthorizationRole.USER),

    // OBSERVER or higher
    DRAW("draw", "Redraw the game board", AuthorizationRole.OBSERVER),
    LEAVE("leave", "Leave the current game", AuthorizationRole.OBSERVER),

    // PLAYER
    MAKE_MOVE("move", "Make a move", AuthorizationRole.PLAYER),
    RESIGN("resign", "Resign the game", AuthorizationRole.PLAYER),
    HIGHLIGHT_MOVES("highlight", "Highlight available moves", AuthorizationRole.PLAYER),

    // CONSOLE or higher
    INVALID("invalid", "CONSOLE COMMAND: 'INVALID'", AuthorizationRole.CONSOLE),
    IDENTITY("identity", "CONSOLE COMMAND: 'IDENTITY", AuthorizationRole.CONSOLE),
    NO_INPUT("", "CONSOLE COMMAND: 'NO_INPUT'", AuthorizationRole.CONSOLE);

    private final String commandID;
    private final String description;
    private final AuthorizationRole[] acceptedAuthRoles;
    public static final Command[] UI_COMMANDS = {HELP, QUIT, REGISTER, LOGIN, LOGOUT, CREATE_GAME, LIST_GAMES,
            JOIN_GAME, OBSERVE_GAME, DRAW, LEAVE, MAKE_MOVE, RESIGN, HIGHLIGHT_MOVES};


    Command(String commandID, String description, AuthorizationRole acceptedAuthRole) {
        this.commandID = commandID;
        this.description = description;
        this.acceptedAuthRoles = new AuthorizationRole[]{acceptedAuthRole};
    }

    String getCommandID() {
        return this.commandID;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCommandString() {
        return this.getCommandID();
    }


    public boolean canBeRunBy(AuthorizationRole role) {
        for (AuthorizationRole acceptedRole : acceptedAuthRoles) {
            if (role.hasPermission(acceptedRole)) {
                return true;
            }
        }
        return false;
    }

    public static Command parse(String input) {
        for (Command cmd : UI_COMMANDS) {
            if (cmd.getCommandID().equals(input)) {
                return cmd;
            }
        }

        if ("".equals(input)) {
            return NO_INPUT;
        } else if (TEST.commandID.equals(input)) {
            return TEST;
        } else {
            return INVALID;
        }
    }

}