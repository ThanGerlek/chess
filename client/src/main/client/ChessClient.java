package client;

import client.ui.Command;

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
        switch (cmd) {
            case NO_INPUT -> askForInput();
            case INVALID -> rejectInput();

            case HELP -> printHelpMenu();
            case QUIT -> quit();
            case REGISTER -> register();
            case LOGIN -> login();
            case LOGOUT -> logout();
            case CREATE_GAME -> createGame();
            case LIST_GAMES -> listGames();
            case JOIN_GAME -> joinGame();
            case OBSERVE_GAME -> observeGame();

            case IDENTITY -> {
                return;
            }

        }
    }

    private void askForInput() {
        printStream.println("Please enter a command. Type 'help' to see available commands.");
    }

    private void rejectInput() {
        printStream.println("Unrecognized command. Type 'help' to see available commands.");
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

    public String getStatus() {
        return (state == LoginState.LOGGED_IN) ? "Signed In" : "Signed out";
    }
}
