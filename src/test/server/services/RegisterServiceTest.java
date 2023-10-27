package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.http.RegisterRequest;

class RegisterServiceTest extends ServiceTest {
    private RegisterService service;

    // TODO 200, 400 bad req, 403 taken, 500

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new RegisterService(authDAO, userDAO);
    }

    // Positive test
    @Test
    void register_new_user_returns_okay() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "mail1"));
        // TODO test
    }

    // Negative test
    @Test
    void register_existing_user_returns_already_taken() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "mail1"));
        // TODO test
    }

    @Test
    void register_with_empty_username_returns_bad_request_error() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "mail1"));
        // TODO test
    }

    @Test
    void register_with_empty_password_returns_bad_request_error() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "mail1"));
        // TODO test
    }
}