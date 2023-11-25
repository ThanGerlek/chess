package client.ui.command;

public abstract class Commands {
    public static UserCommand HELP = new UserCommand("help", "Print a list of available commands");
    public static UserCommand QUIT = new UserCommand("quit", "Quit the game");
    public static UserCommand REGISTER = new UserCommand("register", "Register a new user account");
    public static UserCommand LOGIN = new UserCommand("login", "Log in an existing user");
    public static UserCommand LOGOUT = new UserCommand("logout", "Log out the current user");
    public static UserCommand CREATE_GAME = new UserCommand("create", "Create a new chess game");
    public static UserCommand LIST_GAMES = new UserCommand("list", "List all games");
    public static UserCommand JOIN_GAME = new UserCommand("join", "Join an existing game");
    public static UserCommand OBSERVE_GAME = new UserCommand("observe", "Observe an existing game as a spectator");
    public static Command INVALID = new Command("invalid");
    public static Command IDENTITY = new Command("identity");
    public static Command NO_INPUT = new Command("");

    public static Command[] COMMANDS =
            {HELP, QUIT, REGISTER, LOGIN, LOGOUT, CREATE_GAME, LIST_GAMES, JOIN_GAME, OBSERVE_GAME, INVALID, IDENTITY,
                    NO_INPUT};
    public static UserCommand[] USER_COMMANDS =
            {HELP, QUIT, REGISTER, LOGIN, LOGOUT, CREATE_GAME, LIST_GAMES, JOIN_GAME, OBSERVE_GAME};

    public static Command parse(String input) {
        for (UserCommand userCommand : USER_COMMANDS) {
            if (userCommand.getCommandID().equals(input)) {
                return userCommand;
            }
        }

        if ("".equals(input)) {
            return NO_INPUT;
        } else {
            return INVALID;
        }
    }
}