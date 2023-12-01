package client.websocket;

import chess.ChessGame;
import client.ChessClient;
import client.ui.ConsoleUI;
import client.ui.EscapeSequences;
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

    public void handleMessage(ServerMessage serverMessage) {
        ui.println("Received serverMessage of type " + serverMessage.getServerMessageType().name());
        switch (serverMessage.getServerMessageType()) {
            case NOTIFICATION -> displayNotification((NotificationServerMessage) serverMessage);
            case ERROR -> displayError((ErrorServerMessage) serverMessage);
            case LOAD_GAME -> loadGame((LoadGameServerMessage) serverMessage);
        }
    }

    private void displayNotification(NotificationServerMessage notification) {
        // TODO
        ui.println("ServerMessageHandler.displayNotification()");

        String format = EscapeSequences.SET_TEXT_ITALIC;
        String reset = EscapeSequences.RESET_TEXT_ITALIC;
        ui.println(format + notification.getMessage() + reset);
    }

    private void displayError(ErrorServerMessage error) {
        // TODO
        ui.println("ServerMessageHandler.displayError()");

        String format = EscapeSequences.SET_TEXT_ITALIC + EscapeSequences.SET_TEXT_COLOR_RED;
        String reset =
                EscapeSequences.RESET_TEXT_ITALIC + EscapeSequences.RESET_TEXT_AND_BG; // TODO don't reset background?
        ui.println(format + error.getErrorMessage() + reset);
    }

    private void loadGame(LoadGameServerMessage loadGameMessage) {
        // TODO
        ui.println("ServerMessageHandler.loadGame()");

        ChessGame game = loadGameMessage.getGame();
        client.drawBoard(game.getBoard());
    }
}
