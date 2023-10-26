package server.services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import server.AuthToken;
import server.http.MessageResponse;

/**
 * Provides the Logout service, which logs out an existing user.
 */
public class LogoutService {
    private static final AuthDAO authDAO = new MemoryAuthDAO();

    /**
     * Log out a currently logged-in user by invalidating the given token.
     *
     * @param token the AuthToken to invalidate.
     * @return a MessageResponse representing the resulting HTTP response.
     */
    public MessageResponse logout(AuthToken token) {
        try {
            authDAO.removeAuthToken(token);
            return new MessageResponse(200, "Okay!");
        } catch (DataAccessException e) {
            return new MessageResponse(500, String.format("Error: %s", e.getMessage()));
        }
    }

    /*

| **Headers**          | `authorization: <authToken>`                    |
| **Success response** | [200]                                           |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`    |
| **Failure response** | [500] `{ "message": "Error: description" }`     |
     */

}
