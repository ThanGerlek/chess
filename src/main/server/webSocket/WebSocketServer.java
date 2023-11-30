package server.webSocket;

import http.ChessSerializer;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketServer {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand gameCommand = ChessSerializer.gson().fromJson(message, UserGameCommand.class);
        System.out.printf("Received UserGameCommand of type %s. Broadcasting back%n",
                gameCommand.getCommandType().name());
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
}