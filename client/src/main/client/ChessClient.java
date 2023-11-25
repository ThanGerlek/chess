package client;

import client.ui.command.Command;
import client.ui.command.Commands;
import client.ui.command.UserCommand;

import java.io.PrintStream;

import static client.ui.EscapeSequences.*;

public class ChessClient {
    private final String serverURL;
    private final PrintStream printStream;
    private LoginState state;

    public ChessClient(String serverURL, PrintStream printStream) {
        this.serverURL = serverURL;
        this.printStream = printStream;
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
            askForInput();
        } else {
            rejectInput();
        }
    }

    private void printHelpMenu() {
        printStream.println("Available commands:");
        for (UserCommand userCommand : Commands.USER_COMMANDS) {
            printCommandHelp(userCommand);
        }
    }

    private void quit() {
        printStream.println("Goodbye!");
    }

    private void register() {
        printStream.println("register()");
        // TODO
    }

    private void login() {
        printStream.println("login()");
        // TODO
    }

    private void logout() {
        printStream.println("logout()");
        // TODO
    }

    private void createGame() {
        printStream.println("createGame()");
        // TODO
    }

    private void listGames() {
        printStream.println("listGames()");
        // TODO
    }

    private void joinGame() {
        printStream.println("joinGame()");
        // TODO
    }

    private void observeGame() {
        printStream.println("observeGame()");
        // TODO
    }

    private void askForInput() {
        printStream.println("Please enter a command. Type 'help' to see available commands.");
    }

    private void rejectInput() {
        printStream.println("Unrecognized command. Type 'help' to see available commands.");
    }

    private void printCommandHelp(UserCommand cmd) {
        // TODO. pretty-ify
        printStream.printf("\t%s - %s\n", SET_TEXT_BOLD + cmd.getCommandString() + RESET_TEXT_BOLD_FAINT,
                SET_TEXT_ITALIC + cmd.getDescription() + RESET_TEXT_ITALIC);
    }

    public String getStatus() {
        return (state == LoginState.LOGGED_IN) ? "Signed In" : "Signed out";
    }
}
