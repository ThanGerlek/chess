package server.services;

import server.http.JoinGameRequest;
import server.http.MessageResponse;

/**
 * Provides the Join Game service, which connects a user to an existing game as either a player or spectator. This
 * request is idempotent.
 */
public class JoinGameService {

    /**
     * Connect a user to an existing game as either a player or spectator. Verifies that the specified game exists, and,
     * if a color is specified, adds the caller as the requested color to the game. If no color is specified the user is
     * joined as an observer. This request is idempotent.
     *
     * @param request a JoinGameRequest representing the HTTP request.
     * @return a MessageResponse representing the resulting HTTP response.
     */
    public MessageResponse joinGame(JoinGameRequest request) {
        return null;
    }

}
