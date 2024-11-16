package http;

/**
 * A record representing an HTTP response to an authorization request. Used by LoginService and RegisterService.
 *
 * @param authToken a new authorization string
 * @param username  the username of the user the token represents
 * @param message   the response message
 */
public record AuthResponse(String authToken, String username, String message) {
}
