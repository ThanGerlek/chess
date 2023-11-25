package client.connection;

public class FailedResponseException extends Exception {
    public FailedResponseException(String msg) {
        super(msg);
    }
}
