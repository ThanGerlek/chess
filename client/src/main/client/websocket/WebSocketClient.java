package client.websocket;

import client.httpConnection.FailedConnectionException;
import http.ChessSerializer;
import jakarta.websocket.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint {
    private Session session;
    private NotificationHandler notificationHandler;
    private final String serverURL;

    public WebSocketClient(String serverURL) {
        this.session = null;
        this.notificationHandler = null;
        this.serverURL = serverURL;
    }

    public void openConnection(NotificationHandler notificationHandler) throws FailedConnectionException {
        this.notificationHandler = notificationHandler;
        // TODO close previous connection?
        try {
            URI socketURI = getURI(serverURL);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(getMessageHandler());
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new FailedConnectionException("Failed to create WebSocket connection: " + e.getMessage());
        }
    }

    private URI getURI(String serverURL) throws URISyntaxException {
        String uriString = serverURL.replace("https", "ws").replace("http", "ws") + "/connect";
        return new URI(uriString);
    }

    private MessageHandler.Whole<String> getMessageHandler() {
        return s -> {
            ServerMessage serverMessage = ChessSerializer.gson().fromJson(s, ServerMessage.class);
            notificationHandler.notify(serverMessage);
        };
    }

    public void send(UserGameCommand gameCommand) throws FailedConnectionException {
        String messageJson = ChessSerializer.gson().toJson(gameCommand);
        try {
            session.getBasicRemote().sendText(messageJson);
        } catch (IOException e) {
            throw new FailedConnectionException(
                    "Client failed to send WebSocket message with error: " + e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
