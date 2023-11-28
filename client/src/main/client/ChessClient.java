package client;

import chess.ChessBoard;
import chess.ChessBoardImpl;
import chess.ChessGame;
import client.connection.ChessServerFacade;
import client.connection.FailedConnectionException;
import client.connection.FailedResponseException;
import client.ui.BoardDrawer;
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
    private final SessionData sessionData;

    public ChessClient(String serverURL, ConsoleUI ui) {
        this.ui = ui;
        this.serverFacade = new ChessServerFacade(serverURL);
        this.sessionData = new SessionData();
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
        } else if (Commands.DRAW.equals(cmd)) {
            drawBoard();
        } else if (Commands.IDENTITY.equals(cmd)) {
            // do nothing
            return;
        } else if (Commands.NO_INPUT.equals(cmd)) {
            askForCommandInput();
        } else {
            rejectInput();
        }
    }

    private boolean isAuthorizedToRun(Command cmd) {
        return sessionData.getAuthLevel().hasPermission(cmd.getMinRequiredAuthLevel());
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
            sessionData.setUserData(response.authToken(), username);
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
        }
    }

    private void login() {
        String username = ui.promptInput("Username: ");
        String password = ui.promptInput("Password: ");

        try {
            AuthResponse response = serverFacade.login(username, password);
            sessionData.setUserData(response.authToken(), username);
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
        }
    }

    private void logout() {
        try {
            serverFacade.logout(sessionData.getAuthTokenString());
            sessionData.clearUserData();
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
        }
    }

    private void createGame() {
        String gameName = ui.promptInput("Enter a name for this game: ");
        try {
            int gameID = serverFacade.createGame(gameName, sessionData.getAuthTokenString());
            ui.println(String.valueOf(gameID));
            // TODO. automatically run 'join' command after creation
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
        }
    }

    private ArrayList<GameListItem> listGames() {
        try {
            ArrayList<GameListItem> games = serverFacade.listGames(sessionData.getAuthTokenString());
            printGameList(games);
            return games;
        } catch (FailedConnectionException | FailedResponseException e) {
            printError(e);
            return null;
        }
    }

    private void printGameList(ArrayList<GameListItem> games) {
        if (games.isEmpty()) {
            ui.println("There are no currently active games. Use the 'create' command to add one.");
        } else {
            for (int i = 0; i < games.size(); i++) {
                ui.println(String.format("There are %d currently active games.", games.size()));
                ui.println(formatGameInfoString(i, games.get(i)));
            }
        }
    }

    private String formatGameInfoString(int gameNumber, GameListItem game) {
        String whitePlayer = formatUsernameOutput(game.whiteUsername());
        String blackPlayer = formatUsernameOutput(game.blackUsername());
        return String.format("\t[%d] Game name: '%s', white player: %s, black player: %s", gameNumber, game.gameName(),
                whitePlayer, blackPlayer);
    }

    private String formatUsernameOutput(String username) {
        return (username == null || username.isEmpty()) ? "None" : "'" + username + "'";
    }

    private void joinGame() {
        joinGame(false);
    }

    private void observeGame() {
        joinGame(true);
    }

    private void drawBoard() {
        // TODO
        ChessBoard board = new ChessBoardImpl();
        board.resetBoard();
        BoardDrawer drawer = new BoardDrawer(ui, board);
        drawer.setViewerTeamColor(ChessGame.TeamColor.WHITE);
        drawer.draw();
        drawer.setViewerTeamColor(ChessGame.TeamColor.BLACK);
        drawer.draw();
    }

    private void joinGame(boolean asSpectator) {
        ArrayList<GameListItem> games = listGames();
        if (games != null) {
            try {
                GameJoiner joiner = new GameJoiner(ui, serverFacade, games, sessionData.getAuthTokenString());
                joiner.joinGame(asSpectator);
            } catch (FailedConnectionException | FailedResponseException e) {
                printError(e);
            }
        }
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
        // TODO. Better logging
        System.err.println(e.getMessage());
        ui.println("[ERR] " + e.getMessage());
    }

    public String getStatus() {
        if (sessionData.getAuthLevel().hasPermission(AuthorizationLevel.SUPERUSER)) {
            return "SUPERUSER";
        } else if (sessionData.getAuthLevel().hasPermission(AuthorizationLevel.USER)) {
            return sessionData.getUsername();
        } else {
            return "Guest";
        }
    }
}
