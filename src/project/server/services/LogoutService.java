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
     */
    public MessageResponse logout(AuthToken authToken) throws DataAccessException {
        authDAO.removeAuthToken(authToken);
        return new MessageResponse(200, "Okay!");
        // TODO Auth
    }

    /*

| **Headers**          | `authorization: <authToken>`                    |
| **Success response** | [200]                                           |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`    |
| **Failure response** | [500] `{ "message": "Error: description" }`     |
     */

}
