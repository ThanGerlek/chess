package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.http.JoinGameRequest;

class JoinGameServiceTest extends ServiceTest {
    private final AuthToken token = new AuthToken("1234", "user1");
    private JoinGameService service;

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        authDAO.addAuthToken(token);
        service = new JoinGameService(authDAO, gameDAO);
    }

    @Test
    void join_Game_does_not_error() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHITE", 4), token);
        // TODO test
    }

    @Test
    void join_Game_without_color_does_not_error() throws DataAccessException {
        service.joinGame(new JoinGameRequest(null, 4), token);
        // TODO test
    }

    @Test
    void join_nonexistent_Game_errors() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHITE", 4), token);
        // TODO test
    }

    @Test
    void join_Game_with_invalid_token_errors() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHITE", 4), token);
        // TODO test
    }

}