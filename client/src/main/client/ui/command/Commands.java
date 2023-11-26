package client.ui.command;

public abstract class Commands {
    public static UICommand HELP = new UICommand("help", "Print a list of available commands");
    public static UICommand QUIT = new UICommand("quit", "Quit the game");
    public static UICommand REGISTER = new UICommand("register", "Register a new user account");
    public static UICommand LOGIN = new UICommand("login", "Log in an existing user");
    public static UICommand LOGOUT = new UICommand("logout", "Log out the current user");
    public static UICommand CREATE_GAME = new UICommand("create", "Create a new chess game");
    public static UICommand LIST_GAMES = new UICommand("list", "List all games");
    public static UICommand JOIN_GAME = new UICommand("join", "Join an existing game");
    public static UICommand OBSERVE_GAME = new UICommand("observe", "Observe an existing game as a spectator");
    public static Command INVALID = new Command("invalid");
    public static Command IDENTITY = new Command("identity");
    public static Command NO_INPUT = new Command("");

    public static Command[] COMMANDS =
            {HELP, QUIT, REGISTER, LOGIN, LOGOUT, CREATE_GAME, LIST_GAMES, JOIN_GAME, OBSERVE_GAME, INVALID, IDENTITY,
                    NO_INPUT};
    public static UICommand[] UI_COMMANDS =
            {HELP, QUIT, REGISTER, LOGIN, LOGOUT, CREATE_GAME, LIST_GAMES, JOIN_GAME, OBSERVE_GAME};

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