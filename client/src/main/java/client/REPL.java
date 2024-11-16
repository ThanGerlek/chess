package client;

import httpconnection.FailedConnectionException;
import httpconnection.FailedResponseException;
import ui.ConsoleUI;
import ui.Command;
import websocket.NotificationHandler;
import websocket.ServerMessageHandler;

import java.util.Scanner;

public class REPL implements NotificationHandler {
    private final ConsoleUI ui;
    private final ChessClient client;
    private final ServerMessageHandler serverMessageHandler;

    public REPL(String serverURL) {
        this.ui = new ConsoleUI(new Scanner(System.in), System.out);
        this.client = new ChessClient(serverURL, ui, this);
        this.serverMessageHandler = new ServerMessageHandler(ui, client);
    }

    public void run() {
        ui.println("Welcome! Please enter a command, or 'help' for a list of available commands.");
        Command cmd = Command.IDENTITY;
        while (cmd != Command.QUIT) {
            cmd = getCommand();
            try {
                runCommand(cmd);
            } catch (FailedConnectionException | FailedResponseException e) {
                printError(e);
            }
        }
    }

    private Command getCommand() {
        String input = ui.promptInput(getCommandPrompt());
        return Command.parse(input);
    }

    public void runCommand(Command cmd) throws FailedConnectionException, FailedResponseException {
        if (!client.isAuthorizedToRun(cmd)) {
            client.rejectAuthorization();
            return;
        }

        // TODO replace with switch?
        switch (cmd) {
            case HELP -> client.printHelpMenu();
            case QUIT -> client.quit();
            case REGISTER -> client.register();
            case LOGIN -> client.login();
            case LOGOUT -> client.logout();
            case CREATE_GAME -> client.createGame();
            case LIST_GAMES -> client.listGames();
            case JOIN_GAME -> client.joinGame();
            case OBSERVE_GAME -> client.observeGame();
            case DRAW -> client.drawBoard();
            case LEAVE -> client.leaveGame();
            case MAKE_MOVE -> client.makeMove();
            case RESIGN -> client.resign();
            case HIGHLIGHT_MOVES -> client.highlightMoves();
            case TEST -> client.runTest();
            case NO_INPUT -> askForCommandInput();
            case IDENTITY -> {} // do nothing
            default -> rejectInput();
        }
    }

    private void printError(Exception e) {
        // TODO. Better logging
        ui.println(e.getMessage());
    }

    private String getCommandPrompt() {
        // TODO pretty-ify
        return String.format("[%s] >>> ", getStatus());
    }

    private void askForCommandInput() {
        ui.println("Please enter a command. Type 'help' to see available commands.");
    }

    private void rejectInput() {
        ui.println("Unrecognized command. Type 'help' to see available commands.");
    }

    public String getStatus() {
        SessionData sessionData = client.getSessionData();
        if (sessionData.getAuthRole().hasPermission(AuthorizationRole.SUPERUSER)) {
            return "SUPERUSER";
        } else if (sessionData.getAuthRole().hasPermission(AuthorizationRole.USER)) {
            return sessionData.getUsername();
        } else {
            return "Guest";
        }
    }

    @Override
    public void notify(String message) {
        serverMessageHandler.handleMessage(message);
    }
}
