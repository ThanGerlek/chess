package services;

import http.AuthResponse;
import http.LoginRequest;

/**
 * Provides the Login service, which authenticates an existing user.
 */
public class LoginService {

    /**
     * Logs in an existing user. Returns a new authorization token for that user via an {@link http.AuthResponse}.
     *
     * @param request a {@link http.LoginRequest} representing the HTTP request.
     * @return an {@code AuthResponse} representing the resulting HTTP response.
     */
    public AuthResponse login(LoginRequest request) {
        return null;
    }

}
