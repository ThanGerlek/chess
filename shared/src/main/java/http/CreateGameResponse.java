package http;

/**
 * A record representing an HTTP response to a CreateGameService request.
 *
 * @param gameID  the unique ID of the newly created game
 * @param message the response message
 */
public record CreateGameResponse(Integer gameID, String message) {
}
