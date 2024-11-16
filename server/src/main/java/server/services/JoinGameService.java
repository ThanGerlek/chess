package server.services;

import dataaccess.*;
import dataaccess.exception.BadRequestException;
import dataaccess.exception.DataAccessException;
import dataaccess.exception.UnauthorizedAccessException;
import http.JoinGameRequest;
import http.MessageResponse;

/**
 * Provides the Join Game service, which connects a user to an existing game as a player. This
 * request is idempotent.
 */
public class JoinGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    /**
     * Connect a user to an existing game as a player. Verifies that the specified game exists and adds the caller
     * as the requested color to the game. This request is idempotent.
     *
     * @param request   a JoinGameRequest representing the HTTP request.
     * @param authToken the AuthToken representing the user to assign.
     * @return a MessageResponse representing the resulting HTTP response.
     */
    public MessageResponse joinGame(JoinGameRequest request, String authToken) throws DataAccessException {
        if (request.playerColor() == null) {
            throw new BadRequestException("Could not join game: player color was null");
        }

        if (!authDAO.isValidAuthToken(authToken)) {
            throw new UnauthorizedAccessException("Could not join game: provided token was invalid");
        }

        int gameID = request.gameID();
        String username = authDAO.getUsername(authToken);
        PlayerRole role = PlayerRole.stringToRole(request.playerColor());
        gameDAO.assignPlayerRole(gameID, username, role);

        return new MessageResponse("Okay!");
    }

/*

| **Headers**          | `authorization: <authToken>`
| **Body**             | `{ "playerColor":"WHITE/BLACK", "gameID": 1234 }`
| **Success response** | [200]
| **Failure response** | [400] `{ "message": "Error: bad request" }`
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`
| **Failure response** | [403] `{ "message": "Error: already taken" }`
| **Failure response** | [500] `{ "message": "Error: description" }`
     */

}
