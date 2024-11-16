package service;

import dataaccess.exception.DataAccessException;
import dataaccess.exception.UnauthorizedAccessException;
import http.AuthResponse;
import http.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.LoginService;

class LoginServiceTest extends ServiceTest {
    private LoginService service;

    // TODO 200, 401 forbidden, 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        userDAO.insertNewUser(createTestUser("user1", "pass1", "mail1"));
        service = new LoginService(authDAO, userDAO);
    }

    // Positive test
    @Test
    void loginSuccessfullyReturnsValidAuthToken() throws DataAccessException {
        AuthResponse response = service.login(new LoginRequest("user1", "pass1"));
        Assertions.assertTrue(authDAO.isValidAuthToken(response.authToken()));
    }

    // Negative test
    @Test
    void loginIncorrectPasswordReturnsForbidden() {
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> service.login(new LoginRequest("user1", "iAmIncorrect")));
    }

    @Test
    void loginSuccessfullyReturnsOkay() throws DataAccessException {
        AuthResponse response = service.login(new LoginRequest("user1", "pass1"));
        Assertions.assertEquals("Okay!", response.message());
    }

    @Test
    void loginSuccessfullyReturnsAuthToken() throws DataAccessException {
        AuthResponse response = service.login(new LoginRequest("user1", "pass1"));
        Assertions.assertNotNull(response.authToken());
    }

    @Test
    void loginIncorrectUsernameReturnsForbidden() {
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> service.login(new LoginRequest("iAmIncorrect", "pass1")));
    }

}
