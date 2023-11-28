package client.ui;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final PrintStream printStream;

    public ConsoleUI(Scanner scanner, PrintStream printStream) {
        this.scanner = scanner;
        this.printStream = printStream;
        resetColors();
    }

    public void resetColors() {
        print(EscapeSequences.RESET_TEXT_AND_BG);
    }

    public void println(String string) {
        printStream.println(string);
    }

    public void println() {
        printStream.println();
    }

    public String promptInput(String prompt) {
        print(prompt);
        return sanitize(scanner.nextLine());
    }

    public void print(String string) {
        printStream.print(string);
    }

    private String sanitize(String input) {
        return input.strip().split(" ")[0].strip().toLowerCase();
    }

    public Integer promptMaybeInteger(String prompt) {
        print(prompt);
        String raw = sanitize(scanner.nextLine());
        if (raw.isEmpty()) {
            return null;
        } else {
            return Integer.parseInt(raw);
        }
    }
}
