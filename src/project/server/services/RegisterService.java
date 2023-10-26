package server.services;

import dataAccess.*;
import server.AuthToken;
import server.User;
import server.http.AuthResponse;
import server.http.RegisterRequest;

import java.util.UUID;

// TODO style: move all errorResponse functions into a superclass or something

/**
 * Provides the Register service, which registers a new user.
 */
public class RegisterService {
    private static final UserDAO userDAO = new MemoryUserDAO();
    private static final AuthDAO authDAO = new MemoryAuthDAO();

    /**
     * Register a new user.
     *
     * @param request a RegisterRequest representing the HTTP request.
     * @return an AuthResponse representing the resulting HTTP response.
     */
    public AuthResponse register(RegisterRequest request) {
        User user = new User(request.username(), request.password(), request.email());
        try {
            userDAO.insertNewUser(user);
            AuthToken authToken = registerNewAuthToken(request.username());
            return new AuthResponse(200, authToken, request.username(), "Okay!");
        } catch (ValueAlreadyTakenException e) {
            return errorResponse(403, e);
        } catch (DataAccessException e) {
            return errorResponse(500, e);
        }
    }

    private AuthToken registerNewAuthToken(String username) throws DataAccessException {
        String tokenString = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(tokenString, username);
        authDAO.addAuthToken(authToken);
        return authToken;
    }

    private AuthResponse errorResponse(int status, Exception e) {
        return new AuthResponse(status, null, null, String.format("Error: %s", e.getMessage()));
    }

}
/*

| **Body**             | `{ "username":"", "password":"", "email":"" }` |
| **Success response** | [200] `{ "username":"", "authToken":"" }`      |
| **Failure response** | [400] `{ "message": "Error: bad request" }`    |
| **Failure response** | [403] `{ "message": "Error: already taken" }`  |
| **Failure response** | [500] `{ "message": "Error: description" }`    |
 */