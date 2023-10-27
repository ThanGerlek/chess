package server.services;

import dataAccess.DataAccessException;
import dataAccess.NoSuchItemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;

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

        Assertions.assertFalse(userDAO.hasUser("user1"));
        Assertions.assertFalse(userDAO.hasUser("user2"));
    }

    @Test
    void finding_cleared_Games_errors() throws DataAccessException {
        service.clearApplication();

        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(1));
        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(2));
    }

    @Test
    void cleared_AuthTokens_are_invalid() throws DataAccessException {
        AuthToken token1 = new AuthToken("1234", "user1");
        AuthToken token2 = new AuthToken("2468", "user2");

        service.clearApplication();

        Assertions.assertFalse(authDAO.isValidAuthToken(token1));
        Assertions.assertFalse(authDAO.isValidAuthToken(token2));
    }

    @Test
    void clearing_empty_does_not_error() throws DataAccessException {
        service.clearApplication();
        service.clearApplication();
        Assertions.assertTrue(true);
    }
}