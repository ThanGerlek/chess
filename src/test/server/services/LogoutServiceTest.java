package server.services;

import dataAccess.DataAccessException;
import dataAccess.UnauthorizedAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.User;
import server.http.MessageResponse;

class LogoutServiceTest extends ServiceTest {
    private final User user = new User("user1", "pass1", "mail1");
    private final AuthToken token = new AuthToken("1234", "user1");
    private LogoutService service;

    // TODO 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        userDAO.insertNewUser(user);
        authDAO.addAuthToken(token);

        service = new LogoutService(authDAO);
    }

    // Positive test
    @Test
    void logout_existing_user_returns_okay() throws DataAccessException {
        MessageResponse response = service.logout(token);
//        Assertions.assertEquals(200, response.status());
        // TODO Assert something
    }

    // Negative test
    @Test
    void logout_fake_user_returns_bad_request_error() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> service.logout(new AuthToken("iDoNotExist", "1234")));
    }

    @Test
    void logout_invalid_token_errors() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> service.logout(new AuthToken("1234", "iAmIncorrect")));
    }

}