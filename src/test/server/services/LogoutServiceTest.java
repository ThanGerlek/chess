package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;

class LogoutServiceTest extends ServiceTest {
    private LogoutService service;

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new LogoutService(authDAO);
    }

    @Test
    void logout_existing_user() throws DataAccessException {
        service.logout(new AuthToken("1234", "user1"));
        // TODO test
    }

    @Test
    void logout_fake_user_errors() throws DataAccessException {
        service.logout(new AuthToken("1234", "idontexist"));
        // TODO test
    }

    @Test
    void logout_invalid_token_errors() throws DataAccessException {
        service.logout(new AuthToken("1234", "idontexist"));
        // TODO test
    }

}