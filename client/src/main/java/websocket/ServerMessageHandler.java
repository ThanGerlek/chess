package websocket;

import client.ChessClient;
import http.ChessSerializer;
import model.Game;
import ui.ConsoleUI;
import websocket.messages.ErrorServerMessage;
import websocket.messages.LoadGameServerMessage;
import websocket.messages.NotificationServerMessage;
import websocket.messages.ServerMessage;

public class ServerMessageHandler {
    private final ConsoleUI ui;
    private final ChessClient client;

    public ServerMessageHandler(ConsoleUI ui, ChessClient client) {
        this.ui = ui;
        this.client = client;
    }

    public void handleMessage(String message) {
        ServerMessage serverMessage = ChessSerializer.gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case NOTIFICATION -> parseAsNotification(message);
            case ERROR -> parseAsError(message);
            case LOAD_GAME -> parseAsLoadGame(message);
            default -> ui.println("Problem: " + serverMessage.getServerMessageType().name() +
                    " wasn't recognized as a valid message type");
        }
    }

    private void parseAsNotification(String message) {
        NotificationServerMessage notification =
                ChessSerializer.gson().fromJson(message, NotificationServerMessage.class);
        ui.println("[Info] " + notification.getMessage());
    }

    private void parseAsError(String messageJson) {
        ErrorServerMessage errorMessage = ChessSerializer.gson().fromJson(messageJson, ErrorServerMessage.class);
        ui.println(errorMessage.getErrorMessage());
    }

    private void parseAsLoadGame(String message) {
        LoadGameServerMessage loadGameMessage = ChessSerializer.gson().fromJson(message, LoadGameServerMessage.class);
        Game game = loadGameMessage.getGame();
        client.setCurrentGame(game);
        client.drawBoard();
        ui.reprintPrompt();
    }
}
