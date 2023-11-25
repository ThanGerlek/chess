package client;

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
        Command cmd = getCommand();
        client.runCommand(cmd);
        while (cmd != Commands.QUIT) {
            cmd = getCommand();
            client.runCommand(cmd);
        }
    }

    private Command getCommand() {
        String input = ui.promptInput(getCommandPrompt());
        return Commands.parse(input);
    }

    private String getCommandPrompt() {
        // TODO pretty-ify
        return String.format("[%s] >>> ", client.getStatus());
    }

}
