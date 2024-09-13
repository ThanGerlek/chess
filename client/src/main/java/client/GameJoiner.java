package client;

import chess.ChessGame;
import http.GameListItem;
import httpConnection.ChessServerFacade;
import httpConnection.FailedConnectionException;
import httpConnection.FailedResponseException;
import ui.ConsoleUI;
import ui.IllegalCommandException;
import websocket.commands.ConnectGameCommand;

import java.util.ArrayList;

public class GameJoiner {
    private final ConsoleUI ui;
    private final ChessServerFacade serverFacade;
    private final SessionData sessionData;
    private final ArrayList<GameListItem> games;

    public GameJoiner(ConsoleUI ui, ChessServerFacade serverFacade, SessionData sessionData,
            ArrayList<GameListItem> games) {
        this.ui = ui;
        this.serverFacade = serverFacade;
        this.sessionData = sessionData;
        this.games = games;
    }

    public ConnectGameCommand joinGame() throws FailedResponseException, FailedConnectionException,
            CommandCancelException {
        if (!games.isEmpty()) {
            ui.println("Enter the number for the game you would like to play.");
            int gameID = selectGame();
            ChessGame.TeamColor color = selectColor();

            serverFacade.joinGame(color, gameID, sessionData.getAuthTokenString());

            sessionData.setGameData(gameID, color);
            sessionData.setAuthRole(AuthorizationRole.PLAYER);

            ChessGame.TeamColor playerColor = sessionData.getPlayerColor();
            return buildConnectGameCommand(playerColor);
        } else {
            throw new IllegalCommandException("Can't join a game: no games were found. Try creating a game first.");
        }
    }

    public ConnectGameCommand observeGame() throws CommandCancelException {
        if (!games.isEmpty()) {
            ui.println("Enter the number for the game you would like to observe.");
            int gameID = selectGame();

            sessionData.setGameData(gameID, null);
            sessionData.setAuthRole(AuthorizationRole.OBSERVER);

            return buildConnectGameCommand(null);
        } else {
            throw new IllegalCommandException("Failed to observe: no games are currently being played.");
        }
    }

    private ConnectGameCommand buildConnectGameCommand(ChessGame.TeamColor playerColor) {
        return new ConnectGameCommand(sessionData.getAuthTokenString(), sessionData.getGameID(), playerColor);
    }

    private int selectGame() throws CommandCancelException {
        try { // TODO rmv unneeded try/catch
            Integer gameNumber = ui.promptMaybeInteger("Enter a game number: ");
            if (gameNumber == null) {
                throw new CommandCancelException("Cancelled by player");
            } else if (gameNumber < 0 || gameNumber > games.size()) {
                throw cancelOnInvalidInput(String.format("gameNumber '%d' is out of range", gameNumber));
            } else {
                return games.get(gameNumber).gameID();
            }
        } catch (NumberFormatException e) {
            throw cancelOnInvalidInput(e.getMessage());
        }
    }

    private ChessGame.TeamColor selectColor() throws CommandCancelException {
        String colorString = ui.promptInput("What color would you like to play? Enter 'white' or 'black': ");
        if ("white".equals(colorString) || "w".equals(colorString)) {
            return ChessGame.TeamColor.WHITE;
        } else if ("black".equals(colorString) || "b".equals(colorString)) {
            return ChessGame.TeamColor.BLACK;
        } else {
            throw cancelOnInvalidInput(String.format("Invalid team color string: '%s'", colorString));
        }
    }

    private CommandCancelException cancelOnInvalidInput(String msg) {
        // TODO? Restructure select methods: don't use exceptions for cancelling
        ui.println("Unrecognized value. Cancelling");
        // TODO log this error
        return new CommandCancelException(msg);
    }
}
