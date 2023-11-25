package client;

import client.connection.ChessServerFacade;
import client.ui.ConsoleUI;
import client.ui.command.Command;
import client.ui.command.Commands;
import client.ui.command.UserCommand;

import static client.ui.EscapeSequences.*;

public class ChessClient {
    private final ConsoleUI ui;
    private final ChessServerFacade serverFacade;
    private LoginState state;

    public ChessClient(String serverURL, ConsoleUI ui) {
        this.ui = ui;
        this.serverFacade = new ChessServerFacade(serverURL);
        this.state = LoginState.LOGGED_OUT;
    }

    public void runCommand(Command cmd) {
        if (Commands.HELP.equals(cmd)) {
            printHelpMenu();
        } else if (Commands.QUIT.equals(cmd)) {
            quit();
        } else if (Commands.REGISTER.equals(cmd)) {
            register();
        } else if (Commands.LOGIN.equals(cmd)) {
            login();
        } else if (Commands.LOGOUT.equals(cmd)) {
            logout();
        } else if (Commands.CREATE_GAME.equals(cmd)) {
            createGame();
        } else if (Commands.LIST_GAMES.equals(cmd)) {
            listGames();
        } else if (Commands.JOIN_GAME.equals(cmd)) {
            joinGame();
        } else if (Commands.OBSERVE_GAME.equals(cmd)) {
            observeGame();
        } else if (Commands.IDENTITY.equals(cmd)) {
            return;
        } else if (Commands.NO_INPUT.equals(cmd)) {
            askForCommandInput();
        } else {
            rejectInput();
        }
    }

    private void printHelpMenu() {
        ui.println("Available commands:");
        for (UserCommand userCommand : Commands.USER_COMMANDS) {
            ui.println("\t" + getHelpStringForCommand(userCommand));
        }
    }

    private void quit() {
        ui.println("Goodbye!");
    }

    private void register() {
        ui.println("register()");
        // TODO

    }

    private void login() {
        ui.println("login()");
        // TODO
    }

    private void logout() {
        ui.println("logout()");
        // TODO
    }

    private void createGame() {
        ui.println("createGame()");
        // TODO
    }

    private void listGames() {
        ui.println("listGames()");
        // TODO
    }

    private void joinGame() {
        ui.println("joinGame()");
        // TODO
    }

    private void observeGame() {
        ui.println("observeGame()");
        // TODO
    }

    private void askForCommandInput() {
        ui.println("Please enter a command. Type 'help' to see available commands.");
    }

    private void rejectInput() {
        ui.println("Unrecognized command. Type 'help' to see available commands.");
    }

    private String getHelpStringForCommand(UserCommand cmd) {
        // TODO. pretty-ify
        return String.format("%s - %s", SET_TEXT_BOLD + cmd.getCommandString() + RESET_TEXT_BOLD_FAINT,
                SET_TEXT_ITALIC + cmd.getDescription() + RESET_TEXT_ITALIC);
    }

    public String getStatus() {
        return (state == LoginState.LOGGED_IN) ? "Signed In" : "Signed out";
    }
}
