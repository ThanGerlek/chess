package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.http.JoinGameRequest;

class JoinGameServiceTest extends ServiceTest {
    private JoinGameService service;

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new JoinGameService(gameDAO);
    }

    @Test
    void join_Game_does_not_error() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHTIE", 4), "user1");
        // TODO test
    }

    @Test
    void join_Game_without_color_does_not_error() throws DataAccessException {
        service.joinGame(new JoinGameRequest(null, 4), "user1");
        // TODO test
    }

    @Test
    void join_nonexistent_Game_errors() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHTIE", 4), "user1");
        // TODO test
    }

    @Test
    void join_Game_with_invalid_token_errors() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHTIE", 4), "user1");
        // TODO test
    }

}