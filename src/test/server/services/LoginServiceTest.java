package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.http.LoginRequest;

class LoginServiceTest extends ServiceTest {
    private LoginService service;

    // TODO 200,401 forbidden, 500?

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new LoginService(authDAO, userDAO);
    }

    // Positive test
    @Test
    void login_successfully_returns_okay() throws DataAccessException {
        service.login(new LoginRequest("user1", "pass1"));
        // TODO test
    }

    // Negative test
    @Test
    void login_incorrect_password_returns_forbidden() throws DataAccessException {
        service.login(new LoginRequest("user1", "pass1"));
        // TODO test
    }

    @Test
    void login_incorrect_username_errors() throws DataAccessException {
        service.login(new LoginRequest("user1", "pass1"));
        // TODO test
    }

}