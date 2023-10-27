package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.http.CreateGameRequest;

class CreateGameServiceTest extends ServiceTest {
    private CreateGameService service;

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new CreateGameService(authDAO, gameDAO);
    }

    @Test
    void create_Game() throws DataAccessException {
        service.createGame(new CreateGameRequest("game1"), new AuthToken("1234", "user"));
        // TODO test
    }

    @Test
    void create_Game_with_invalid_token_errors() throws DataAccessException {
        service.createGame(new CreateGameRequest("game1"), new AuthToken("1234", "iDoNotExist"));
        // TODO test
    }

}