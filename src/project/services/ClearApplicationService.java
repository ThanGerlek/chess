package services;

import http.MessageResponse;

/**
 * Provides the Clear Application service, which performs a hard reset of the database, erasing all users, games, and
 * auth tokens.
 */
public class ClearApplicationService {

    /**
     * Clears the application. WARNING: This performs a hard reset, erasing all registered users, games, and auth
     * tokens.
     *
     * @return a {@link http.MessageResponse} representing the resulting HTTP response.
     */
    public MessageResponse clearApplication() {
        return null;
    }

}
