package server.services;

import server.http.CreateGameRequest;
import server.http.CreateGameResponse;
import server.AuthToken;

/**
 * Provides the Create New Game service, which registers and initializes a new empty game.
 */
public class CreateGameService {

    /**
     * Create a new game from the given CreateGameRequest.
     *
     * @param request a CreateGameRequest representing the HTTP request.
     * @param token   the AuthToken provided with the request.
     * @return a CreateGameResponse representing the resulting HTTP response.
     */
    public CreateGameResponse createGame(CreateGameRequest request, AuthToken token) {
        return null;
    }

}
