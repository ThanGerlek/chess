package server.services;

import dataaccess.AuthDAO;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import http.AuthResponse;
import http.RegisterRequest;
import model.AuthToken;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

/**
 * Provides the Register service, which registers a new user.
 */
public class RegisterService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public RegisterService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    /**
     * Register a new user.
     *
     * @param request a RegisterRequest representing the HTTP request.
     * @return an AuthResponse representing the resulting HTTP response.
     */
    public AuthResponse register(RegisterRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null || request.username().isEmpty() || request.password().isEmpty()) {
            throw new BadRequestException("Please provide a username and password");
        }
        String pwHash = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        User user = new User(request.username(), pwHash, request.email());
        userDAO.insertNewUser(user);
        AuthToken authToken = registerNewAuthToken(request.username());
        return new AuthResponse(authToken.authToken(), request.username(), "Okay!");
    }

    private AuthToken registerNewAuthToken(String username) throws DataAccessException {
        String tokenString = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(tokenString, username);
        authDAO.addAuthToken(authToken);
        return authToken;
    }
}

/*

| **Body**             | `{ "username":"", "password":"", "email":"" }` |
| **Success response** | [200] `{ "username":"", "authToken":"" }`      |
| **Failure response** | [400] `{ "message": "Error: bad request" }`    |
| **Failure response** | [403] `{ "message": "Error: already taken" }`  |
| **Failure response** | [500] `{ "message": "Error: description" }`    |
 */