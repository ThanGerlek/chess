package ui;

import client.CommandCancelException;

public class IllegalCommandException extends CommandCancelException {
    public IllegalCommandException(String msg) {
        super(msg);
    }
}
