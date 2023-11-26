package client;

import chess.ChessGame;
import client.connection.ChessServerFacade;
import client.connection.FailedConnectionException;
import client.connection.FailedResponseException;
import client.ui.ConsoleUI;
import http.GameListItem;

import java.util.ArrayList;

public class GameJoiner {
    private ConsoleUI ui;
    private ChessServerFacade serverFacade;
    private ArrayList<GameListItem> games;
    private String authTokenString;

    public GameJoiner(ConsoleUI ui, ChessServerFacade serverFacade, ArrayList<GameListItem> games,
            String authTokenString) {
        this.ui = ui;
        this.serverFacade = serverFacade;
        this.games = games;
        this.authTokenString = authTokenString;
    }

    public void joinGame(boolean asSpectator) throws FailedResponseException, FailedConnectionException {
        ui.println("Enter the number for the game you would like to play.");
        try {
            int gameID = selectGame();
            ChessGame.TeamColor color = asSpectator ? null : selectColor();
            serverFacade.joinGame(color, gameID, authTokenString);
        } catch (CommandCancelException ignored) {
        }
    }

    private int selectGame() throws CommandCancelException {
        try {
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
