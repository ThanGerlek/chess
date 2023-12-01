package server.webSocket;

import http.ChessSerializer;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WebSocketServer {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand gameCommand = ChessSerializer.gson().fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()) {
            case JOIN_PLAYER -> parseAsJoinPlayer(session, message);
            case JOIN_OBSERVER -> parseAsJoinObserver(session, message);
            case MAKE_MOVE -> parseAsMakeMove(session, message);
            case LEAVE -> parseAsLeave(session, message);
            case RESIGN -> parseAsResign(session, message);
        }

        ServerMessage serverMessage = new NotificationServerMessage(
                String.format("Server received a UserGameCommand of type %s", gameCommand.getCommandType().name()));
        send(session, serverMessage);
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

    private void parseAsJoinObserver(Session session, String message) {
        JoinObserverGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinObserverGameCommand.class);

        System.out.printf("JOIN_OBSERVER | gameID: %d", gameCommand.getGameID());
    }

    private void parseAsJoinPlayer(Session session, String message) {
        JoinPlayerGameCommand gameCommand = ChessSerializer.gson().fromJson(message, JoinPlayerGameCommand.class);

        System.out.printf("JOIN_PLAYER | gameID: %d, color: %s", gameCommand.getGameID(),
                gameCommand.getPlayerColor().name());
    }

    private void parseAsMakeMove(Session session, String message) {
        MakeMoveGameCommand gameCommand = ChessSerializer.gson().fromJson(message, MakeMoveGameCommand.class);

        System.out.printf("MAKE_MOVE | gameID: %d, move: %d.%d to %d.%d", gameCommand.getGameID(),
                gameCommand.getMove().getStartPosition().getRow(), gameCommand.getMove().getStartPosition().getColumn(),
                gameCommand.getMove().getEndPosition().getRow(), gameCommand.getMove().getEndPosition().getColumn());
    }

    private void parseAsLeave(Session session, String message) {
        LeaveGameCommand gameCommand = ChessSerializer.gson().fromJson(message, LeaveGameCommand.class);

        System.out.printf("LEAVE | gameID: %d", gameCommand.getGameID());
    }

    private void parseAsResign(Session session, String message) {
        ResignGameCommand gameCommand = ChessSerializer.gson().fromJson(message, ResignGameCommand.class);

        System.out.printf("RESIGN | gameID: %d", gameCommand.getGameID());
    }
}