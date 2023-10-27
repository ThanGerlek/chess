package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.http.RegisterRequest;

class RegisterServiceTest extends ServiceTest {
    private RegisterService service;

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new RegisterService(authDAO, userDAO);
    }

    @Test
    void register_new_user_adds_user_to_database() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "mail1"));
        // TODO test
    }

    @Test
    void register_existing_user_errors() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "mail1"));
        // TODO test
    }

    @Test
    void register_with_empty_username_errors() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "mail1"));
        // TODO test
    }

    @Test
    void register_with_empty_password_errors() throws DataAccessException {
        service.register(new RegisterRequest("user1", "pass1", "mail1"));
        // TODO test
    }
}