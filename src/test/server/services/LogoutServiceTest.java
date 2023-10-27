package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;

class LogoutServiceTest extends ServiceTest {
    private LogoutService service;

    // TODO 200, 401 forbidden, 500?

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new LogoutService(authDAO);
    }

    // Positive test
    @Test
    void logout_existing_user_returns_okay() throws DataAccessException {
        service.logout(new AuthToken("1234", "user1"));
        // TODO test
    }

    // Negative test
    @Test
    void logout_fake_user_returns_bad_request_error() throws DataAccessException {
        service.logout(new AuthToken("1234", "idontexist"));
        // TODO test
    }

    @Test
    void logout_invalid_token_errors() throws DataAccessException {
        service.logout(new AuthToken("1234", "idontexist"));
        // TODO test
    }

}