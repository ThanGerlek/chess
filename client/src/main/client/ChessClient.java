package client;

import client.connection.ChessServerFacade;
import client.connection.FailedConnectionException;
import client.connection.FailedResponseException;
import client.ui.ConsoleUI;
import client.ui.command.Command;
import client.ui.command.Commands;
import client.ui.command.UICommand;
import http.AuthResponse;
import http.GameListItem;

import java.util.ArrayList;

import static client.ui.EscapeSequences.*;

public class ChessClient {
    private final ConsoleUI ui;
    private final ChessServerFacade serverFacade;
    private AuthorizationLevel authLevel;
    // TODO store sign in info

    public ChessClient(String serverURL, ConsoleUI ui) {
        this.ui = ui;
        this.serverFacade = new ChessServerFacade(serverURL);
        this.authLevel = AuthorizationLevel.ANY;
    }

    public void runCommand(Command cmd) {
        if (!isAuthorizedToRun(cmd)) {
            rejectAuthorization();
            return;
        }

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

    private boolean isAuthorizedToRun(Command cmd) {
        return authLevel.hasPermission(cmd.getMinRequiredAuthLevel());
    }

    private void rejectAuthorization() {
        ui.println("Woah! You're not allowed to do that right now. Try logging in first.");
    }

    private void printHelpMenu() {
        ui.println("Available commands:");
        for (UICommand cmd : Commands.UI_COMMANDS) {
            if (isAuthorizedToRun(cmd)) {
                ui.println("\t" + getHelpStringForCommand(cmd));
            }
        }
    }

    private void quit() {
        ui.println("Goodbye!");
    }

    private void register() {
        ui.println("Please enter a username and password.");
        ui.println(String.format(
                "%sWARNING: DO NOT USE A REAL PASSWORD.%s This program was built by a college undergrad, not a " +
                        "security" + " professional. It is NOT secure.", SET_TEXT_BOLD, RESET_TEXT_BOLD_FAINT));
        String username = ui.promptInput("Username: ");
        String password = ui.promptInput("Password: ");
        String email = ui.promptInput("Email (optional): ");

        try {
            AuthResponse response = serverFacade.register(username, password, email);
            ui.println(String.valueOf(response));
            // TODO
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
        }
    }

    private void login() {
        String username = ui.promptInput("Username: ");
        String password = ui.promptInput("Password: ");

        try {
            AuthResponse response = serverFacade.login(username, password);
            ui.println(String.valueOf(response));
            // TODO
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
        }
    }

    private void logout() {
        try {
            serverFacade.logout(getTokenString());
            // TODO
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
        }
    }

    private void createGame() {
        String gameName = ui.promptInput("Enter a name for this game: ");
        try {
            int gameID = serverFacade.createGame(gameName, getTokenString());
            ui.println(String.valueOf(gameID));
            // TODO
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
        }
    }

    private ArrayList<GameListItem> listGames() {
        try {
            ArrayList<GameListItem> games = serverFacade.listGames(getTokenString());
            ui.println(String.valueOf(games));
            // TODO
            return games;
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
            return null;
        }
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

    private String getHelpStringForCommand(UICommand cmd) {
        // TODO. pretty-ify
        return String.format("%s - %s", SET_TEXT_BOLD + cmd.getCommandString() + RESET_TEXT_BOLD_FAINT,
                SET_TEXT_ITALIC + cmd.getDescription() + RESET_TEXT_ITALIC);
    }

    private void printError(Exception e) {
        // TODO Better logging
        System.err.println(e.getMessage());
        ui.println("[ERR] " + e.getMessage());
    }

    private String getTokenString() {
        // TODO
        return "1234";
    }

    public String getStatus() {
        if (authLevel.hasPermission(AuthorizationLevel.SUPERUSER)) {
            return "SUPERUSER";
        } else if (authLevel.hasPermission(AuthorizationLevel.USER)) {
            // TODO return username
            return "User";
        } else {
            return "Guest";
        }
    }
}
