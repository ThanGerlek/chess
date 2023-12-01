package server.webSocket;

import dataAccess.*;
import http.ChessSerializer;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorServerMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketServer {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final UserGameCommandHandler cmdHandler;

    public WebSocketServer(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.cmdHandler = new UserGameCommandHandler(authDAO, gameDAO, userDAO);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand gameCommand = ChessSerializer.gson().fromJson(message, UserGameCommand.class);
        try {
            switch (gameCommand.getCommandType()) {
                case JOIN_PLAYER -> cmdHandler.parseAsJoinPlayer(session, message);
                case JOIN_OBSERVER -> cmdHandler.parseAsJoinObserver(session, message);
                case MAKE_MOVE -> cmdHandler.parseAsMakeMove(session, message);
                case LEAVE -> cmdHandler.parseAsLeave(session, message);
                case RESIGN -> cmdHandler.parseAsResign(session, message);
            }
        } catch (UnauthorizedAccessException e) {
            sendError(session, "Invalid token. Are you logged in correctly?");
        } catch (DataAccessException e) {
            sendError(session, "Sorry, there's an unknown problem. Please try again.");
        }
    }

    @OnWebSocketError
    public void onWebSocketError(Session session, Throwable exception) {
        System.err.println("Server threw uncaught WebSocket error: " + exception.getMessage());
    }

    private void send(Session session, ServerMessage serverMessage) {
        String messageJson = ChessSerializer.gson().toJson(serverMessage);
        try {
            session.getRemote().sendString(messageJson);
        } catch (IOException e) {
            System.err.println("Failed to send WebSocket message with error: " + e.getMessage());
        }
    }

    private void sendError(Session session, String errMsg) {
        System.err.println("Server threw error while parsing UserGameCommand: " + errMsg);
        send(session, new ErrorServerMessage(errMsg));
    }

}