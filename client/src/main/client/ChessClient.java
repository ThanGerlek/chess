package client;

import client.ui.command.Command;
import client.ui.command.Commands;

import java.io.PrintStream;

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
        // TODO
    }

    private void quit() {
        // TODO
    }

    private void register() {
        // TODO
    }

    private void login() {
        // TODO
    }

    private void logout() {
        // TODO
    }

    private void createGame() {
        // TODO
    }

    private void listGames() {
        // TODO
    }

    private void joinGame() {
        // TODO
    }

    private void observeGame() {
        // TODO
    }

    private void askForInput() {
        printStream.println("Please enter a command. Type 'help' to see available commands.");
    }

    private void rejectInput() {
        printStream.println("Unrecognized command. Type 'help' to see available commands.");
    }

    public String getStatus() {
        return (state == LoginState.LOGGED_IN) ? "Signed In" : "Signed out";
    }
}
