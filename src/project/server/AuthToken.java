package server;

/**
 * An authorization token for the given user.
 *
 * @param authToken the unique token string
 * @param username  the user this AuthToken represents
 */
public record AuthToken(String authToken, String username) {
}
