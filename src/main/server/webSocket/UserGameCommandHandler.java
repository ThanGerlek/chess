package server.webSocket;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import http.ChessSerializer;
import webSocketMessages.userCommands.*;

public class UserGameCommandHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public UserGameCommandHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public void parseAsJoinObserver(GameSessionManager sessionManager, String message) {
        JoinObserverGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinObserverGameCommand.class);

        System.out.printf("JOIN_OBSERVER | gameID: %d%n", gameCommand.getGameID());
    }

    public void parseAsJoinPlayer(Session session, String message) {
        JoinPlayerGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinPlayerGameCommand.class);

        System.out.printf("JOIN_PLAYER | gameID: %d, color: %s%n", gameCommand.getGameID(),
                gameCommand.getPlayerColor().name());
    }

    public void parseAsMakeMove(GameSessionManager sessionManager, String message) {
        MakeMoveGameCommand gameCommand = ChessSerializer.gson().fromJson(message, MakeMoveGameCommand.class);

        System.out.printf("MAKE_MOVE | gameID: %d, move: %d.%d to %d.%d%n", gameCommand.getGameID(),
                gameCommand.getMove().getStartPosition().getRow(), gameCommand.getMove().getStartPosition().getColumn(),
                gameCommand.getMove().getEndPosition().getRow(), gameCommand.getMove().getEndPosition().getColumn());
    }

    public void parseAsLeave(GameSessionManager sessionManager, String message) {
        LeaveGameCommand gameCommand = ChessSerializer.gson().fromJson(message, LeaveGameCommand.class);

        System.out.printf("LEAVE | gameID: %d%n", gameCommand.getGameID());
    }

    public void parseAsResign(GameSessionManager sessionManager, String message) {
        ResignGameCommand gameCommand = ChessSerializer.gson().fromJson(message, ResignGameCommand.class);

        System.out.printf("RESIGN | gameID: %d%n", gameCommand.getGameID());
    }
}
