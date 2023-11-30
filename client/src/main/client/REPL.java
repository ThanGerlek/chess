package client;

import client.httpConnection.FailedConnectionException;
import client.httpConnection.FailedResponseException;
import client.ui.ConsoleUI;
import client.ui.command.Command;
import client.ui.command.Commands;

import java.util.Scanner;

public class REPL {
    private final ConsoleUI ui;
    private final ChessClient client;

    public REPL(String serverURL) {
        this.ui = new ConsoleUI(new Scanner(System.in), System.out);
        this.client = new ChessClient(serverURL, ui);
    }

    public void run() {
        ui.println("Welcome! Please enter a command, or 'help' for a list of available commands.");
        Command cmd = Commands.IDENTITY;
        while (cmd != Commands.QUIT) {
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
        return Commands.parse(input);
    }

    private String getCommandPrompt() {
        // TODO pretty-ify
        return String.format("[%s] >>> ", getStatus());
    }

    public void runCommand(Command cmd) throws FailedConnectionException, FailedResponseException {
        if (!client.isAuthorizedToRun(cmd)) {
            client.rejectAuthorization();
            return;
        }

        if (Commands.HELP.equals(cmd)) {
            client.printHelpMenu();
        } else if (Commands.TEST.equals(cmd)) {
            client.test();
        } else if (Commands.QUIT.equals(cmd)) {
            client.quit();
        } else if (Commands.REGISTER.equals(cmd)) {
            client.register();
        } else if (Commands.LOGIN.equals(cmd)) {
            client.login();
        } else if (Commands.LOGOUT.equals(cmd)) {
            client.logout();
        } else if (Commands.CREATE_GAME.equals(cmd)) {
            client.createGame();
        } else if (Commands.LIST_GAMES.equals(cmd)) {
            client.listGames();
        } else if (Commands.JOIN_GAME.equals(cmd)) {
            client.joinGame();
        } else if (Commands.OBSERVE_GAME.equals(cmd)) {
            client.observeGame();
        } else if (Commands.DRAW.equals(cmd)) {
            client.drawBoard();
        } else if (Commands.LEAVE.equals(cmd)) {
            client.leaveGame();
        } else if (Commands.MAKE_MOVE.equals(cmd)) {
            client.makeMove();
        } else if (Commands.RESIGN.equals(cmd)) {
            client.resign();
        } else if (Commands.HIGHLIGHT_MOVES.equals(cmd)) {
            client.highlightMoves();
        } else if (Commands.IDENTITY.equals(cmd)) {
            // do nothing
            return;
        } else if (Commands.NO_INPUT.equals(cmd)) {
            askForCommandInput();
        } else {
            rejectInput();
        }
    }

    private void askForCommandInput() {
        ui.println("Please enter a command. Type 'help' to see available commands.");
    }

    private void rejectInput() {
        ui.println("Unrecognized command. Type 'help' to see available commands.");
    }

    private void printError(Exception e) {
        // TODO. Better logging
        System.err.println(e.getMessage());
        ui.println("[ERR] " + e.getMessage());
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

}
