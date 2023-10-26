package server.services;

import dataAccess.*;
import server.http.MessageResponse;

/**
 * Provides the Clear Application service, which performs a hard reset of the database, erasing all users, games, and
 * auth tokens.
 */
public class ClearApplicationService {
    private static final AuthDAO authDAO = new MemoryAuthDAO();
    private static final GameDAO gameDAO = new MemoryGameDAO();
    private static final UserDAO userDAO = new MemoryUserDAO();

    /**
     * Clear the application. WARNING: This performs a hard reset, erasing all registered users, games, and auth
     * tokens.
     *
     * @return a MessageResponse representing the resulting HTTP response.
     */
    public MessageResponse clearApplication() throws DataAccessException {
        authDAO.clearAuthTokens();
        gameDAO.clearGames();
        userDAO.clearUsers();
        return new MessageResponse(200, "Okay!");
    }
}
