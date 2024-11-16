package dataaccess.exception;

public class BadRequestException extends DataAccessException {
    // TODO move this out of DataAccessException
    public BadRequestException(String message) {
        super(message);
    }
}
