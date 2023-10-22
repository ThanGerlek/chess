package server.services;

import server.http.MessageResponse;

/**
 * Provides the Clear Application service, which performs a hard reset of the database, erasing all users, games, and
 * auth tokens.
 */
public class ClearApplicationService {

    /**
     * Clear the application. WARNING: This performs a hard reset, erasing all registered users, games, and auth
     * tokens.
     *
     * @return a MessageResponse representing the resulting HTTP response.
     */
    public MessageResponse clearApplication() {
        return null;
    }

}
