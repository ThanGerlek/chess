package client.websocket;

import webSocketMessages.serverMessages.ServerMessage;

@FunctionalInterface
public interface NotificationHandler {
    void notify(ServerMessage serverMessage);
}
