package client.websocket;

import client.ChessClient;
import client.ui.ConsoleUI;
import http.ChessSerializer;
import model.Game;
import webSocketMessages.serverMessages.ErrorServerMessage;
import webSocketMessages.serverMessages.LoadGameServerMessage;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.serverMessages.ServerMessage;

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
        // TODO

        NotificationServerMessage notification =
                ChessSerializer.gson().fromJson(message, NotificationServerMessage.class);

        ui.println("NotificationServerMessage | " + notification.getMessage());

//        String format = EscapeSequences.SET_TEXT_ITALIC;
//        String reset = EscapeSequences.RESET_TEXT_ITALIC;
//        ui.println(format + notification.getMessage() + reset);
    }

    private void parseAsError(String messageJson) {
        // TODO
        ErrorServerMessage errorMessage = ChessSerializer.gson().fromJson(messageJson, ErrorServerMessage.class);
        ui.println("ServerMessageHandler.parseAsError(): " + errorMessage.getErrorMessage());
    }

    private void parseAsLoadGame(String message) {
        // TODO
        LoadGameServerMessage loadGameMessage = ChessSerializer.gson().fromJson(message, LoadGameServerMessage.class);
        Game game = loadGameMessage.getGame();
        client.drawBoard();
    }
}
