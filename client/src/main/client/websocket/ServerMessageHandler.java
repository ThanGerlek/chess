package client.websocket;

@FunctionalInterface
public interface ServerMessageHandler {
    void handleServerMessage(String serverMessage);
}
