package server.services;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import server.http.ListGamesResponse;

/**
 * Provides the List Games service, which returns data about all games currently in progress.
 */
public class ListGamesService {
    private static final GameDAO gameDAO = new MemoryGameDAO();

    /**
     * Get a list of all games currently in progress.
     *
     * @return a ListGamesResponse representing the resulting HTTP response.
     */
    public ListGamesResponse listGames() throws DataAccessException {
        return new ListGamesResponse(200, gameDAO.allGames(), "Okay!");
    }

/*

| **Headers**          | `authorization: <authToken>`                                                                 |
| **Success response** | [200] `{ "games": ["gameID": 1234, "whiteUsername":"", "blackUsername":"", "gameName:""} ]}` |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`                                                 |
| **Failure response** | [500] `{ "message": "Error: description" }`                                                  |
     */

}
