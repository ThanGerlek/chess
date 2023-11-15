package server.services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import http.MessageResponse;

/**
 * Provides the Clear Application service, which performs a hard reset of the database, erasing all users, games, and
 * auth tokens.
 */
public class ClearApplicationService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public ClearApplicationService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

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
        return new MessageResponse("Okay!");
    }
}
