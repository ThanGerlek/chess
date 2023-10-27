package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClearApplicationServiceTest extends ServiceTest {
    private ClearApplicationService service;

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new ClearApplicationService(authDAO, gameDAO, userDAO);
    }

    @Test
    void has_cleared_Users_is_false() throws DataAccessException {
        service.clearApplication();
        // TODO test
    }

    @Test
    void finding_cleared_Games_errors() throws DataAccessException {
        service.clearApplication();
        // TODO test
    }

    @Test
    void cleared_AuthTokens_are_invalid() throws DataAccessException {
        service.clearApplication();
        // TODO test
    }

    @Test
    void clearing_empty_does_not_error() throws DataAccessException {
        service.clearApplication();
        // TODO test
    }
}