package server.webSocket;

import chess.ChessGame;
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

    public void parseAsJoinObserver(Session session, String message) {
        JoinObserverGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinObserverGameCommand.class);
        System.out.printf("JOIN_OBSERVER | gameID: %d%n", gameCommand.getGameID());
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

    public void parseAsMakeMove(Session session, String message) {
        MakeMoveGameCommand gameCommand = ChessSerializer.gson().fromJson(message, MakeMoveGameCommand.class);
        System.out.printf("MAKE_MOVE | gameID: %d, move: %d.%d to %d.%d%n", gameCommand.getGameID(),
                gameCommand.getMove().getStartPosition().getRow(), gameCommand.getMove().getStartPosition().getColumn(),
                gameCommand.getMove().getEndPosition().getRow(), gameCommand.getMove().getEndPosition().getColumn());
    }

    public void parseAsLeave(Session session, String message) {
        LeaveGameCommand gameCommand = ChessSerializer.gson().fromJson(message, LeaveGameCommand.class);
        System.out.printf("LEAVE | gameID: %d%n", gameCommand.getGameID());
    }

    public void parseAsResign(Session session, String message) {
        ResignGameCommand gameCommand = ChessSerializer.gson().fromJson(message, ResignGameCommand.class);
        System.out.printf("RESIGN | gameID: %d%n", gameCommand.getGameID());
    }

    private void requireValidAuthString(UserGameCommand gameCommand) throws DataAccessException {
        if (!authDAO.isValidAuthToken(gameCommand.getAuthString())) {
            throw new UnauthorizedAccessException("Invalid token provided");
        }
    }
}
