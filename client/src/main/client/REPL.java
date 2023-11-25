package client;

import client.ui.command.Command;
import client.ui.command.Commands;

import java.io.PrintStream;
import java.util.Scanner;

public class REPL {
    private final Scanner scanner;
    private final PrintStream printStream;
    private final ChessClient client;

    public REPL(String serverURL) {
        this.scanner = new Scanner(System.in);
        this.printStream = System.out;
        this.client = new ChessClient(serverURL, printStream);
    }

    public void run() {
        printStream.println("Welcome! Please enter a command, or 'help' for a list of available commands.");
        Command cmd = getCommand();
        client.runCommand(cmd);
        while (cmd != Commands.QUIT) {
            cmd = getCommand();
            client.runCommand(cmd);
        }
    }

    private Command getCommand() {
        printPrompt();
        String input = scanner.nextLine();
        input = sanitize(input);
        return Commands.parse(input);
    }

    private void printPrompt() {
        // TODO pretty-ify
        System.out.printf("[%s] >>> ", client.getStatus());
    }

    private String sanitize(String input) {
        return input.strip().toLowerCase();
    }

}
