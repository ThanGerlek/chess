package server.webSocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class WebSocketServer {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.printf("Received message '%s'. Broadcasting back%n", message);
        try {
            session.getRemote().sendString(String.format("Server: \"I am replying to message '%s'\"", message));
        } catch (IOException e) {
            System.out.println("Failed to send WebSocket reply");
            throw e;
        }
    }

    @OnWebSocketError
    public void onWebSocketError(Session session, Throwable exception) throws IOException {
        String msg = "While processing WebSocket message, server threw exception: " + exception.getMessage();
        System.err.println(msg);
        session.getRemote().sendString(msg);
    }
}