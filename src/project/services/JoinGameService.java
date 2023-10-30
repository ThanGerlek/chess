package services;

import http.JoinGameRequest;
import http.MessageResponse;

/**
 * Provides the Join Game service, which connects a user to an existing game as either a player or spectator. This
 * request is idempotent.
 */
public class JoinGameService {

    /**
     * Connects a user to an existing game as either a player or spectator. Verifies that the specified game exists,
     * and, if a color is specified, adds the caller as the requested color to the game. If no color is specified the
     * user is joined as an observer. This request is idempotent.
     *
     * @param request a {@link http.JoinGameRequest} representing the HTTP request.
     * @return a {@link http.MessageResponse} representing the resulting HTTP response.
     */
    public MessageResponse joinGame(JoinGameRequest request) {
        return null;
    }

}
