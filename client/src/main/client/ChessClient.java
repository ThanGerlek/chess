package client;

import chess.ChessBoard;
import chess.ChessBoardImpl;
import chess.ChessGame;
import client.httpConnection.ChessServerFacade;
import client.httpConnection.FailedConnectionException;
import client.httpConnection.FailedResponseException;
import client.ui.BoardDrawer;
import client.ui.ConsoleUI;
import client.ui.command.Command;
import client.ui.command.Commands;
import client.ui.command.UICommand;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketClient;
import http.AuthResponse;
import http.GameListItem;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.LeaveGameCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.ArrayList;

import static client.ui.EscapeSequences.*;

public class ChessClient {
    private final ConsoleUI ui;
    private final ChessServerFacade serverFacade;
    private final WebSocketClient ws;
    private final SessionData sessionData;

    public ChessClient(String serverURL, ConsoleUI ui) {
        this.ui = ui;
        this.serverFacade = new ChessServerFacade(serverURL);
        this.ws = new WebSocketClient(serverURL);
        this.sessionData = new SessionData();
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public boolean isAuthorizedToRun(Command cmd) {
        return cmd.canBeRunBy(sessionData.getAuthRole());
    }

    public void rejectAuthorization() {
        ui.println("Woah! You're not allowed to do that right now. Try logging in first.");
    }

    public void test() throws FailedConnectionException {
        ui.println("This command is for testing purposes only and is subject to change at any time.");

        String thing = ui.promptInput("? ");

        if ("connect".equals(thing)) {
            testCreateConnection();
        } else if ("send".equals(thing)) {
            testSendMessage();
        }
    }

    private void testCreateConnection() throws FailedConnectionException {
        ui.println("Creating WS connection to server");
        NotificationHandler notificationHandler = (serverMessage) -> {
            System.out.printf("Received server message '%s'%n", serverMessage);
            throw new RuntimeException("Problem!");
        };

        ws.openConnection(notificationHandler);
        ui.println("Connection created. Listening");
    }

    private void testSendMessage() throws FailedConnectionException {
        ui.println("Sending message");
        UserGameCommand gameCommand = new LeaveGameCommand("myAuthToken", 3);
        ws.send(gameCommand);

    }

    public void printHelpMenu() {
        ui.println("Available commands:");
        for (UICommand cmd : Commands.UI_COMMANDS) {
            if (isAuthorizedToRun(cmd)) {
                ui.println("\t" + getHelpStringForCommand(cmd));
            }
        }
    }

    public void quit() {
        ui.println("Goodbye!");
    }

    public void register() throws FailedConnectionException, FailedResponseException {
        ui.println("Please enter a username and password.");
        ui.println(String.format(
                "%sWARNING: DO NOT USE A REAL PASSWORD.%s This program was built by a college undergrad, not a " +
                        "security" + " professional. It is NOT secure.", SET_TEXT_BOLD, RESET_TEXT_BOLD_FAINT));
        String username = ui.promptInput("Username: ");
        String password = ui.promptInput("Password: ");
        String email = ui.promptInput("Email (optional): ");

        AuthResponse response = serverFacade.register(username, password, email);
        sessionData.setUserData(response.authToken(), username);
        sessionData.setAuthRole(AuthorizationRole.GUEST);
    }

    public void login() throws FailedConnectionException, FailedResponseException {
        String username = ui.promptInput("Username: ");
        String password = ui.promptInput("Password: ");

        AuthResponse response = serverFacade.login(username, password);
        sessionData.setUserData(response.authToken(), username);
        sessionData.setAuthRole(AuthorizationRole.USER);
    }

    public void logout() throws FailedConnectionException, FailedResponseException {
        serverFacade.logout(sessionData.getAuthTokenString());
        sessionData.clearUserData();

    }

    public void createGame() throws FailedConnectionException, FailedResponseException {
        String gameName = ui.promptInput("Enter a name for this game: ");
        serverFacade.createGame(gameName, sessionData.getAuthTokenString());
    }

    public ArrayList<GameListItem> listGames() throws FailedConnectionException, FailedResponseException {
        ArrayList<GameListItem> games = serverFacade.listGames(sessionData.getAuthTokenString());
        printGameList(games);
        return games;
    }

    private void printGameList(ArrayList<GameListItem> games) {
        if (games.isEmpty()) {
            ui.println("There are no currently active games. Use the 'create' command to add one.");
        } else {
            ui.println(String.format("Currently active games: %d", games.size()));
            for (int i = 0; i < games.size(); i++) {
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

    public void joinGame() throws FailedConnectionException, FailedResponseException {
        joinGame(false);
    }

    public void observeGame() throws FailedConnectionException, FailedResponseException {
        joinGame(true);
    }

    public void drawBoard() {
        ChessBoard board = new ChessBoardImpl();
        board.resetBoard();
        BoardDrawer drawer = new BoardDrawer(ui, board);
        drawer.setViewerTeamColor(ChessGame.TeamColor.WHITE);
        drawer.draw();
        drawer.setViewerTeamColor(ChessGame.TeamColor.BLACK);
        drawer.draw();
    }

    private void joinGame(boolean asSpectator) throws FailedConnectionException, FailedResponseException {
        ArrayList<GameListItem> games = listGames();
        if (games != null) {
            try {
                GameJoiner joiner = new GameJoiner(ui, serverFacade, games, sessionData.getAuthTokenString());
                joiner.joinGame(asSpectator);
                sessionData.setAuthRole(AuthorizationRole.OBSERVER);
                drawBoard();
            } catch (CommandCancelException ignored) {
            }
        }
    }

    public void leaveGame() {
        // TODO
    }

    public void makeMove() {
        // TODO
    }

    public void resign() {
        // TODO
    }

    public void highlightMoves() {
        // TODO
    }

    private String getHelpStringForCommand(UICommand cmd) {
        return String.format("%s - %s", SET_TEXT_BOLD + cmd.getCommandString() + RESET_TEXT_BOLD_FAINT,
                SET_TEXT_ITALIC + cmd.getDescription() + RESET_TEXT_ITALIC);
    }
}
