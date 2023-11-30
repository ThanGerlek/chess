package client.ui.command;

import client.AuthorizationRole;

public abstract class Commands {
    // ANY or higher
    public static UICommand HELP = new UICommand("help", "Print a list of available commands", AuthorizationRole.ANY);
    public static UICommand QUIT = new UICommand("quit", "Quit the game", AuthorizationRole.ANY);

    // GUEST
    public static UICommand REGISTER =
            new UICommand("register", "Register a new user account", AuthorizationRole.GUEST);
    public static UICommand LOGIN = new UICommand("login", "Log in an existing user", AuthorizationRole.GUEST);

    // USER or higher
    public static UICommand LOGOUT = new UICommand("logout", "Log out the current user", AuthorizationRole.USER);
    public static UICommand CREATE_GAME = new UICommand("create", "Create a new chess game", AuthorizationRole.USER);
    public static UICommand LIST_GAMES = new UICommand("list", "List all games", AuthorizationRole.USER);
    public static UICommand JOIN_GAME = new UICommand("join", "Join an existing game", AuthorizationRole.USER);
    public static UICommand OBSERVE_GAME =
            new UICommand("observe", "Observe an existing game as a spectator", AuthorizationRole.USER);

    public static UICommand DRAW = new UICommand("draw", "Draw the game board", AuthorizationRole.USER);

    // CONSOLE or higher
    public static Command INVALID = new Command("invalid", AuthorizationRole.CONSOLE);
    public static Command IDENTITY = new Command("identity", AuthorizationRole.CONSOLE);
    public static Command NO_INPUT = new Command("", AuthorizationRole.CONSOLE);

    public static UICommand[] UI_COMMANDS =
            {HELP, QUIT, REGISTER, LOGIN, LOGOUT, CREATE_GAME, LIST_GAMES, JOIN_GAME, OBSERVE_GAME, DRAW};

    public static Command parse(String input) {
        for (UICommand cmd : UI_COMMANDS) {
            if (cmd.getCommandID().equals(input)) {
                return cmd;
            }
        }

        if ("".equals(input)) {
            return NO_INPUT;
        } else {
            return INVALID;
        }
    }
}