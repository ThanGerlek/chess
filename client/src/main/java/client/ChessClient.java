package client;

import chess.ChessMove;
import chess.ChessPosition;
import http.AuthResponse;
import http.GameListItem;
import httpconnection.ChessServerFacade;
import httpconnection.FailedConnectionException;
import httpconnection.FailedResponseException;
import model.Game;
import ui.BoardDrawer;
import ui.ConsoleUI;
import ui.IllegalCommandException;
import ui.InvalidUserInputException;
import ui.Command;
import websocket.NotificationHandler;
import websocket.WebSocketClient;
import websocket.commands.ConnectGameCommand;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveGameCommand;
import websocket.commands.ResignGameCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final ConsoleUI ui;
    private final ChessServerFacade serverFacade;
    private final WebSocketClient ws;
    private final SessionData sessionData;
    private Game game;

    public ChessClient(String serverURL, ConsoleUI ui, NotificationHandler notificationHandler) {
        this.ui = ui;
        this.serverFacade = new ChessServerFacade(serverURL);
        this.ws = new WebSocketClient(serverURL, notificationHandler);
        this.sessionData = new SessionData();
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void rejectAuthorization() {
        ui.println("Woah! You're not allowed to do that right now. Try logging in first.");
    }

    public void setCurrentGame(Game game) {
        this.game = game;
    }

    public void printHelpMenu() {
        ui.println("Available commands:");
        for (Command cmd : Command.UI_COMMANDS) {
            if (isAuthorizedToRun(cmd)) {
                ui.println("\t" + getHelpStringForCommand(cmd));
            }
        }
    }

    public boolean isAuthorizedToRun(Command cmd) {
        return cmd.canBeRunBy(sessionData.getAuthRole());
    }

    private String getHelpStringForCommand(Command cmd) {
        return String.format("%s - %s", SET_TEXT_BOLD + cmd.getCommandString() + RESET_TEXT_BOLD_FAINT,
                SET_TEXT_ITALIC + cmd.getDescription() + RESET_TEXT_ITALIC);
    }

    public void quit() {
        ui.println("Goodbye!");
    }

    public void runTest() {
        ui.println("Running test.");
        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
            ui.println(e.getMessage());
        }
        ui.println("Completed test.");
    }

    private void test() throws Exception {
        throw new RuntimeException("Unimplemented test() command");
    }

    public void register() throws FailedConnectionException, FailedResponseException {
        ui.println("Please enter a username and password.");
        ui.println(String.format("%sWARNING: DO NOT USE A REAL PASSWORD.%s This program was built by a college "
                + "undergrad, not a security professional. It is NOT secure.", SET_TEXT_BOLD, RESET_TEXT_BOLD_FAINT));
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

    public void joinGame() throws FailedConnectionException, FailedResponseException {
        ArrayList<GameListItem> games = listGames();
        ConnectGameCommand gameCommand;
        try {
            gameCommand = new GameJoiner(ui, serverFacade, sessionData, games).joinGame();
        } catch (IllegalCommandException | InvalidUserInputException e) {
            ui.println(e.getMessage());
            return;
        } catch (UserCancelException e) {
            return;
        }
        ws.send(gameCommand);
    }

    public void observeGame() throws FailedConnectionException, FailedResponseException {
        ArrayList<GameListItem> games = listGames();
        ConnectGameCommand gameCommand;
        try {
            gameCommand = new GameJoiner(ui, serverFacade, sessionData, games).observeGame();
        } catch (IllegalCommandException | InvalidUserInputException e) {
            ui.println(e.getMessage());
            return;
        } catch (UserCancelException e) {
            return;
        }

        ws.send(gameCommand);
    }

    public ArrayList<GameListItem> listGames() throws FailedConnectionException, FailedResponseException {
        // TODO make sure this never returns null
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
        return String.format("\t[%d] Game name: '%s', white player: %s, black player: %s",
                gameNumber, game.gameName(), whitePlayer, blackPlayer);
    }

    private String formatUsernameOutput(String username) {
        return (username == null || username.isEmpty()) ? "None" : "'" + username + "'";
    }

    public void drawBoard() {
        BoardDrawer drawer = new BoardDrawer(ui, game.chessGame().getBoard(), sessionData.getPlayerColor());
        drawer.draw();
    }

    public void highlightMoves() {
        ChessPosition startPosition;
        try {
            startPosition = ui.promptChessPosition("Enter the position of a chess piece: ");
        } catch (InvalidUserInputException e) {
            ui.println(String.format("Unrecognized value '%s'. Cancelling", e.getInvalidInputString()));
            return;
        } catch (UserCancelException e) {
            return;
        }

        Collection<ChessMove> validMoves = game.chessGame().validMoves(startPosition);

        if (validMoves == null) {
            ui.println("That isn't a piece you can move.");
            return;
        }

        Collection<ChessPosition> highlightedPositions = new HashSet<>();
        for (ChessMove move : validMoves) {
            highlightedPositions.add(move.getEndPosition());
        }

        BoardDrawer drawer = new BoardDrawer(ui, game.chessGame().getBoard(), sessionData.getPlayerColor());
        drawer.drawWithHighlightedPositions(highlightedPositions);
        if (highlightedPositions.isEmpty()) {
            ui.println("That piece has no valid moves.");
        }
    }

    public void leaveGame() throws FailedConnectionException {
        ws.send(new LeaveGameCommand(sessionData.getAuthTokenString(), sessionData.getGameID()));
        // TODO Race condition?
//        ws.closeConnection();
        sessionData.clearGameData();
        sessionData.setAuthRole(AuthorizationRole.USER);
    }

    public void makeMove() throws FailedConnectionException {
        ChessMove move;
        try {
            move = ui.promptChessMove();
        } catch (InvalidUserInputException e) {
            ui.println(String.format("Unrecognized value '%s'. Cancelling", e.getInvalidInputString()));
            return;
        } catch (UserCancelException e) {
            return;
        }

        ws.send(new MakeMoveGameCommand(sessionData.getAuthTokenString(), sessionData.getGameID(), move));
    }

    public void resign() throws FailedConnectionException {
        String input = ui.promptInput("Are you sure you want to resign? This cannot be undone. "
                + "Enter 'confirm' if so, or anything else to " + "cancel: ");
        if ("confirm".equals(input)) {
            ws.send(new ResignGameCommand(sessionData.getAuthTokenString(), sessionData.getGameID()));
            sessionData.setAuthRole(AuthorizationRole.OBSERVER);
        }
    }

}
