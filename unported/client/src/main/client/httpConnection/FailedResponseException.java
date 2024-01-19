package client.httpConnection;

public class FailedResponseException extends Exception {
    public FailedResponseException(String msg) {
        super(msg);
    }
}
