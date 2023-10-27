package server.services;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import server.AuthToken;
import server.http.ListGamesResponse;

/**
 * Provides the List Games service, which returns data about all games currently in progress.
 */
public class ListGamesService {
    private final GameDAO gameDAO;

    public ListGamesService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    /**
     * Get a list of all games currently in progress.
     *
     * @return a ListGamesResponse representing the resulting HTTP response.
     */
    public ListGamesResponse listGames(AuthToken authToken) throws DataAccessException {
        return new ListGamesResponse(200, gameDAO.allGames(), "Okay!");
        // TODO Auth
    }

/*

| **Headers**          | `authorization: <authToken>`                                                                 |
| **Success response** | [200] `{ "games": ["gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}` |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`                                                 |
| **Failure response** | [500] `{ "message": "Error: description" }`                                                  |
     */

}
