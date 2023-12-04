package server.webSocket;

import chess.ChessGame;
import chess.ChessGameImpl;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.*;
import http.ChessSerializer;
import model.Game;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.LoadGameServerMessage;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.userCommands.*;

import java.util.Objects;

public class UserGameCommandHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final GameSessionManager sessionManager;
    private final WebSocketServer wsServer;

    public UserGameCommandHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO, WebSocketServer wsServer) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.sessionManager = new GameSessionManager(wsServer);
        this.wsServer = wsServer;
    }

    public void parseAsJoinObserver(Session session, String message) throws DataAccessException {
        JoinObserverGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinObserverGameCommand.class);
        System.out.printf("JOIN_OBSERVER | gameID: %d%n", gameCommand.getGameID());

        requireValidAuthString(gameCommand);

        String username = authDAO.getUsername(gameCommand.getAuthString());
        int gameID = gameCommand.getGameID();
        Game game = gameDAO.findGame(gameID);

        sessionManager.addUser(gameID, username, session);

        LoadGameServerMessage loadGameMsg = new LoadGameServerMessage(game);
        wsServer.send(session, loadGameMsg);

        String notifyStr = String.format("User %s has joined the game as an observer", username);
        NotificationServerMessage notifyMsg = new NotificationServerMessage(notifyStr);
        sessionManager.broadcast(gameID, username, notifyMsg);
    }

    public void parseAsJoinPlayer(Session session, String message) throws DataAccessException {
        JoinPlayerGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinPlayerGameCommand.class);
        System.out.printf("JOIN_PLAYER | gameID: %d, color: %s%n", gameCommand.getGameID(),
                gameCommand.getPlayerColor().name());

        requireValidAuthString(gameCommand);

        String username = authDAO.getUsername(gameCommand.getAuthString());
        int gameID = gameCommand.getGameID();
        Game game = gameDAO.findGame(gameID);

        PlayerRole role;
        String assignedUsername;
        if (gameCommand.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            role = PlayerRole.WHITE_PLAYER;
            assignedUsername = game.whiteUsername();
        } else {
            role = PlayerRole.BLACK_PLAYER;
            assignedUsername = game.blackUsername();
        }

        if (!Objects.equals(username, assignedUsername)) {
            wsServer.sendError(session, "Sorry, that role is already taken.");
            return;
        }

        sessionManager.addUser(gameID, username, session);

        LoadGameServerMessage loadGameMsg = new LoadGameServerMessage(game);
        wsServer.send(session, loadGameMsg);

        String roleString = PlayerRole.roleToString(role);
        String notifyStr = String.format("User %s has joined the game as %s", username, roleString);
        NotificationServerMessage notifyMsg = new NotificationServerMessage(notifyStr);
        sessionManager.broadcast(gameID, username, notifyMsg);
    }

    public void parseAsMakeMove(Session session, String message) throws DataAccessException {
        MakeMoveGameCommand gameCommand = ChessSerializer.gson().fromJson(message, MakeMoveGameCommand.class);
        System.out.printf("MAKE_MOVE | gameID: %d, move: %d.%d to %d.%d%n", gameCommand.getGameID(),
                gameCommand.getMove().getStartPosition().getRow(), gameCommand.getMove().getStartPosition().getColumn(),
                gameCommand.getMove().getEndPosition().getRow(), gameCommand.getMove().getEndPosition().getColumn());

        requireValidAuthString(gameCommand);

        ChessGame.TeamColor playerColor = requireColor(gameCommand.getAuthString(), gameCommand.getGameID());
        if (playerColor == null) {
            wsServer.sendError(session, "Only active players can make moves.");
            return;
        }

        Game game = gameDAO.findGame(gameCommand.getGameID());
        ChessGame chessGame = game.chessGame();
        if (!playerColor.equals(chessGame.getTeamTurn())) {
            wsServer.sendError(session, "It's the other player's turn right now.");
        }

        ChessMove move = gameCommand.getMove();
        try {
            chessGame.makeMove(move);
            gameDAO.updateGameState(game);
        } catch (InvalidMoveException e) {
            wsServer.sendError(session, e, "Invalid move.");
            return;
        }

        LoadGameServerMessage serverMessage = new LoadGameServerMessage(game);
        sessionManager.broadcastAll(gameCommand.getGameID(), serverMessage);
    }

    public void parseAsLeave(Session session, String message) throws DataAccessException {
        LeaveGameCommand gameCommand = ChessSerializer.gson().fromJson(message, LeaveGameCommand.class);
        System.out.printf("LEAVE | gameID: %d%n", gameCommand.getGameID());

        String username = authDAO.getUsername(gameCommand.getAuthString());
        String msg = String.format("User %s left the game", username);
        sessionManager.removeUser(gameCommand.getGameID(), username);
        sessionManager.broadcast(gameCommand.getGameID(), username, new NotificationServerMessage(msg));
    }

    public void parseAsResign(Session session, String message) throws DataAccessException {
        ResignGameCommand gameCommand = ChessSerializer.gson().fromJson(message, ResignGameCommand.class);
        System.out.printf("RESIGN | gameID: %d%n", gameCommand.getGameID());

        requireValidAuthString(gameCommand);

        ChessGame.TeamColor playerColor = requireColor(gameCommand.getAuthString(), gameCommand.getGameID());
        if (playerColor == null) {
            wsServer.sendError(session, "Only active players can resign.");
            return;
        }

        Game game = gameDAO.findGame(gameCommand.getGameID());
        ChessGame chessGame = game.chessGame();
        ((ChessGameImpl) chessGame).resign(playerColor); // TODO Why do I need to cast?

        String username = authDAO.getUsername(gameCommand.getAuthString());
        String msg = String.format("User %s (%s) has resigned the game.", username, playerColor.name());
        NotificationServerMessage serverMessage = new NotificationServerMessage(msg);
        sessionManager.broadcastAll(gameCommand.getGameID(), serverMessage);
    }

    private void requireValidAuthString(UserGameCommand gameCommand) throws DataAccessException {
        if (!authDAO.isValidAuthToken(gameCommand.getAuthString())) {
            throw new UnauthorizedAccessException("Invalid token provided");
        }
    }

    private ChessGame.TeamColor requireColor(String authString, int gameID)
            throws DataAccessException {
        String username = authDAO.getUsername(authString);
        Game game = gameDAO.findGame(gameID);

        ChessGame.TeamColor playerColor;
        if (Objects.equals(username, game.whiteUsername())) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(username, game.blackUsername())) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
        return playerColor;
    }
}
