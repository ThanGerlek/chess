package client.websocket;

import client.httpConnection.FailedConnectionException;
import jakarta.websocket.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClient extends Endpoint {
    private Session session;
    private ServerMessageHandler serverMessageHandler;
    private final String serverURL;

    public WebSocketClient(String serverURL) {
        this.session = null;
        this.serverMessageHandler = null;
        this.serverURL = serverURL;
    }

    public void openConnection(ServerMessageHandler serverMessageHandler) throws FailedConnectionException {
        this.serverMessageHandler = serverMessageHandler;
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
        return new MyMessageHandler(System.out);
    }

    private void parseServerMessageString(String messageJson) {
//        ServerMessage message = ChessSerializer.gson().fromJson(messageJson, ServerMessage.class);
        serverMessageHandler.handleServerMessage(messageJson);
    }

    public void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private record MyMessageHandler(PrintStream printStream) implements MessageHandler.Whole<String> {
        @Override
        public void onMessage(String s) {
            printStream.printf("Received server message '%s'%n", s);
        }
    }
}
