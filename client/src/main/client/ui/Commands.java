package client.ui;

public abstract class Commands {

    public static Command[] USER_COMMANDS =
            {Command.HELP, Command.QUIT, Command.REGISTER, Command.LOGIN, Command.LOGOUT, Command.CREATE_GAME,
                    Command.LIST_GAMES, Command.JOIN_GAME, Command.OBSERVE_GAME};

    public static Command parse(String input) {
        if ("".equals(input)) {
            return Command.NO_INPUT;
        } else if ("help".equals(input)) {
            return Command.HELP;
        } else if ("quit".equals(input)) {
            return Command.QUIT;
        } else if ("register".equals(input)) {
            return Command.REGISTER;
        } else if ("login".equals(input)) {
            return Command.LOGIN;
        } else if ("logout".equals(input)) {
            return Command.LOGOUT;
        } else if ("create".equals(input)) {
            return Command.CREATE_GAME;
        } else if ("list".equals(input)) {
            return Command.LIST_GAMES;
        } else if ("join".equals(input)) {
            return Command.JOIN_GAME;
        } else if ("observe".equals(input)) {
            return Command.OBSERVE_GAME;
        } else {
            return Command.INVALID;
        }
    }
}
