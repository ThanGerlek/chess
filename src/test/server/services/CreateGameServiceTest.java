package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.http.CreateGameRequest;

class CreateGameServiceTest extends ServiceTest {
    private CreateGameService service;

    // TODO 200, 400 bad req, 401 forbidden, 500?

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new CreateGameService(authDAO, gameDAO);
    }

    // Positive test
    @Test
    void create_Game_returns_okay() throws DataAccessException {
        service.createGame(new CreateGameRequest("game1"), new AuthToken("1234", "user"));
        // TODO test
    }

    // Negative test
    @Test
    void create_Game_with_invalid_token_returns_forbidden() throws DataAccessException {
        service.createGame(new CreateGameRequest("game1"), new AuthToken("1234", "iDoNotExist"));
        // TODO test
    }

}