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

    private void parseAsError(String message) {
        // TODO
        ui.println("ServerMessageHandler.parseAsError()");

        ErrorServerMessage errorMessage = ChessSerializer.gson().fromJson(message, ErrorServerMessage.class);

        ui.println("ErrorServerMessage | " + errorMessage.getErrorMessage());

//        String format = EscapeSequences.SET_TEXT_ITALIC + EscapeSequences.SET_TEXT_COLOR_RED;
//        String reset =
//                EscapeSequences.RESET_TEXT_ITALIC + EscapeSequences.RESET_TEXT_AND_BG; // TODO don't reset background?
//        ui.println(format + errorMessage.getErrorMessage() + reset);
    }

    private void parseAsLoadGame(String message) {
        // TODO
        ui.println("ServerMessageHandler.parseAsLoadGame()");

        LoadGameServerMessage loadGameMessage = ChessSerializer.gson().fromJson(message, LoadGameServerMessage.class);

        ui.println("LoadGameServerMessage | ChessGame");

        Game game = loadGameMessage.getGame();
        client.drawBoard();
    }
}
