package server.webSocket;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import http.ChessSerializer;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.*;

public class UserGameCommandHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final GameSessionManager sessionManager;

    public UserGameCommandHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO,
            GameSessionManager sessionManager) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.sessionManager = sessionManager;
    }

    public void parseAsJoinObserver(Session session, String message) {
        JoinObserverGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinObserverGameCommand.class);
        System.out.printf("JOIN_OBSERVER | gameID: %d%n", gameCommand.getGameID());
    }

    public void parseAsJoinPlayer(Session session, String message) {
        JoinPlayerGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinPlayerGameCommand.class);
        System.out.printf("JOIN_PLAYER | gameID: %d, color: %s%n", gameCommand.getGameID(),
                gameCommand.getPlayerColor().name());
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
}
