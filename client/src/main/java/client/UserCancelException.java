package client;

public class UserCancelException extends CommandCancelException {
    public UserCancelException() {
        super("Command cancelled by player");
    }
}

