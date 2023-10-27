package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.http.LoginRequest;

class LoginServiceTest extends ServiceTest {
    private LoginService service;

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new LoginService(authDAO, userDAO);
    }

    @Test
    void login_successfully() throws DataAccessException {
        service.login(new LoginRequest("user1", "pass1"));
        // TODO test
    }

    @Test
    void login_incorrect_password_errors() throws DataAccessException {
        service.login(new LoginRequest("user1", "pass1"));
        // TODO test
    }

    @Test
    void login_incorrect_username_errors() throws DataAccessException {
        service.login(new LoginRequest("user1", "pass1"));
        // TODO test
    }

}