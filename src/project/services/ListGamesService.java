package services;

import http.ListGamesResponse;
import models.AuthToken;

/**
 * Provides the List Games service, which returns data about all games currently in progress.
 */
public class ListGamesService {

    /**
     * Get a list of all games currently in progress.
     *
     * @param token the AuthToken provided with the request.
     * @return a ListGamesResponse representing the resulting HTTP response.
     */
    public ListGamesResponse listGames(AuthToken token) {
        return null;
    }

}
