package server.services;

import dataAccess.*;
import server.AuthToken;
import server.User;
import server.http.AuthResponse;
import server.http.LoginRequest;

import java.util.UUID;

/**
 * Provides the Login service, which authenticates an existing user.
 */
public class LoginService {
    private static final UserDAO userDAO = new MemoryUserDAO();
    private static final AuthDAO authDAO = new MemoryAuthDAO();

    /**
     * Log in an existing user. Returns a new authToken for that user.
     *
     * @param request a LoginRequest representing the HTTP request.
     * @return an AuthResponse representing the resulting HTTP response.
     */
    public AuthResponse login(LoginRequest request) {
        try {
            User user = userDAO.getUser(request.username());
            AuthToken token = authenticate(user, request.password());
            return new AuthResponse(200, token, request.username(), "Okay!");
        } catch (NoSuchItemException | UnauthorizedAccessException e) {
            return errorResponse(401, e);
        } catch (DataAccessException e) {
            return errorResponse(500, e);
        }
    }

    private AuthToken authenticate(User user, String password) throws DataAccessException {
        if (!user.password().equals(password)) {
            throw new UnauthorizedAccessException("Invalid credentials.");
        }
        AuthToken token = registerNewAuthToken(user.username());
        authDAO.addAuthToken(token);
        return token;
    }

    private AuthResponse errorResponse(int status, Exception e) {
        return new AuthResponse(status, null, null, String.format("Error: %s", e.getMessage()));
    }

    // TODO Consolidate with RegisterService's registerNewAuthToken() method
    private AuthToken registerNewAuthToken(String username) throws DataAccessException {
        String tokenString = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(tokenString, username);
        authDAO.addAuthToken(authToken);
        return authToken;
    }

    /*

| **Body**             | `{ "username":"", "password":"" }`                  |
| **Success response** | [200] `{ "username":"", "authToken":"" }`           |
| **Failure response** | [401] `{ "message": "Error: unauthorized" }`        |
| **Failure response** | [500] `{ "message": "Error: description" }`         |
     */

}
