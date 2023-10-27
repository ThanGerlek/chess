package server.services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import server.AuthToken;
import server.http.MessageResponse;

/**
 * Provides the Logout service, which logs out an existing user.
 */
public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    /**
     * Log out a currently logged-in user by invalidating the given token.
     *
     * @param authToken the AuthToken to invalidate.
     * @return a MessageResponse representing the resulting HTTP response.
     * @throws DataAccessException if the token was already invalid
     */
    public MessageResponse logout(AuthToken authToken) throws DataAccessException {
        if (authDAO.isValidAuthToken(authToken)) {
            authDAO.removeAuthToken(authToken);
            return new MessageResponse(200, "Okay!");
        } else {
            throw new DataAccessException("Could not log in: provided token is invalid");
        }
    }

    /*

| **Headers**          | `authorization: <authToken>`                    |
| **Success response** | [200]                                           |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`    |
| **Failure response** | [500] `{ "message": "Error: description" }`     |
     */

}
