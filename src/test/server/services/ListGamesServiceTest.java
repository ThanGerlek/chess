package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListGamesServiceTest extends ServiceTest {
    private ListGamesService service;

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new ListGamesService(gameDAO);
    }

    @Test
    void list_Games_successfully() throws DataAccessException {
        service.listGames();
        // TODO test
    }

    @Test
    void list_Games_with_invalid_token_errors() throws DataAccessException {
        service.listGames();
        // TODO test
    }

}