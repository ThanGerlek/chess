package httpconnection;

public class FailedResponseException extends Exception {
    public FailedResponseException(String msg) {
        super(msg);
    }
}
