package server.services;

import dataAccess.DataAccessException;
import dataAccess.UnauthorizedAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import server.User;
import server.http.AuthResponse;
import server.http.LoginRequest;

class LoginServiceTest extends ServiceTest {
    private LoginService service;

    // TODO 200, 401 forbidden, 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        userDAO.insertNewUser(new User("user1", "pass1", "mail1"));
        service = new LoginService(authDAO, userDAO);
    }

    // Positive test
    @Test
    void login_successfully_returns_okay() throws DataAccessException {
        AuthResponse response = service.login(new LoginRequest("user1", "pass1"));
//        Assertions.assertEquals(200, response.status());
        // TODO Assert something
    }

    // Negative test
    @Test
    void login_incorrect_password_returns_forbidden() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> service.login(new LoginRequest("user1", "iAmIncorrect")));
    }

    @Test
    @Disabled
        // TODO test
    void login_incorrect_username_returns_forbidden() throws DataAccessException {
        AuthResponse response = service.login(new LoginRequest("iAmIncorrect", "pass1"));
//        Assertions.assertEquals(401, response.status());
        // TODO Assert something
    }

}