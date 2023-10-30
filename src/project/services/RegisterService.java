package services;

import http.AuthResponse;
import http.RegisterRequest;

/**
 * Provides the Register service, which registers a new user.
 */
public class RegisterService {

    /**
     * Registers a new user. Returns a new authorization token for that user via an {@link http.AuthResponse}.
     *
     * @param request a {@link http.RegisterRequest} representing the HTTP request.
     * @return an {@code AuthResponse} representing the resulting HTTP response.
     */
    public AuthResponse register(RegisterRequest request) {
        return null;
    }

}
