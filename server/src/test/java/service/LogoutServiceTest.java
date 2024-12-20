package service;

import dataaccess.exception.DataAccessException;
import dataaccess.exception.UnauthorizedAccessException;
import http.MessageResponse;
import model.AuthToken;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.LogoutService;

class LogoutServiceTest extends ServiceTest {
    private final User user = createTestUser("user1", "pass1", "mail1");
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
    void logoutExistingUserInvalidatesToken() throws DataAccessException {
        service.logout(token.authToken());
        Assertions.assertFalse(authDAO.isValidAuthToken(token.authToken()));
    }

    // Negative test
    @Test
    void logoutFakeUserReturnsBadRequestError() {
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> service.logout(new AuthToken("iDoNotExist", "1234").authToken()));
    }

    @Test
    void logoutExistingUserReturnsOkay() throws DataAccessException {
        MessageResponse response = service.logout(token.authToken());
        Assertions.assertEquals("Okay!", response.message());
    }

    @Test
    void logoutInvalidTokenErrors() {
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> service.logout("iAmIncorrect"));
    }

    @Test
    void logoutTokenTwiceErrors() throws DataAccessException {
        service.logout(token.authToken());
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> service.logout(token.authToken()));
    }

}
