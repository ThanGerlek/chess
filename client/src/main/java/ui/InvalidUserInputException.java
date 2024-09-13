package ui;

import client.CommandCancelException;

public class InvalidUserInputException extends CommandCancelException {
    String invalidInputString;

    public InvalidUserInputException(String invalidInputString, String msg) {
        super(msg);
        this.invalidInputString = invalidInputString;
    }

    public String getInvalidInputString() {
        return invalidInputString;
    }
}
