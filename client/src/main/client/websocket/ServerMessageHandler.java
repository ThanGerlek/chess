package client.websocket;

import webSocketMessages.serverMessages.ServerMessage;

@FunctionalInterface
public interface ServerMessageHandler {
    void handleServerMessage(ServerMessage serverMessage);
}
