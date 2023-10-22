package services;

import http.MessageResponse;
import models.AuthToken;

/**
 * Provides the Logout service, which logs out an existing user.
 */
public class LogoutService {

    /**
     * Log out a currently logged-in user by invalidating the given token.
     *
     * @param token the AuthToken to invalidate.
     * @return a MessageResponse representing the resulting HTTP response.
     */
    public MessageResponse logout(AuthToken token) {
        return null;
    }

}
