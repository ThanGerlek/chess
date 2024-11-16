package http;

/**
 * A record representing a simple HTTP response with only a message header. Used by ClearApplicationService,
 * JoinGameService, and LogoutService.
 *
 * @param message the response message
 */
public record MessageResponse(String message) {
}
