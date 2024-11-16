package dataaccess.exception;

public class UnauthorizedAccessException extends DataAccessException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
