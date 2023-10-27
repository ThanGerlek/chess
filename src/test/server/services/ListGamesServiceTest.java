package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;

class ListGamesServiceTest extends ServiceTest {
    private final AuthToken token = new AuthToken("1234", "user1");
    private ListGamesService service;

    // TODO 200, 401 forbidden, 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        authDAO.addAuthToken(token);
        service = new ListGamesService(authDAO, gameDAO);
    }

    // Positive test
    @Test
    void list_Games_returns_okay() throws DataAccessException {
        service.listGames(token);
        // TODO test
    }

    // Negative test
    @Test
    void list_Games_with_invalid_token_returns_forbidden() throws DataAccessException {
        service.listGames(new AuthToken("1234", "iDoNotExist"));
        // TODO test
    }

}