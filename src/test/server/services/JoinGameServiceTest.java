package server.services;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.http.JoinGameRequest;

class JoinGameServiceTest extends ServiceTest {
    private final AuthToken token = new AuthToken("1234", "user1");
    private JoinGameService service;

    // TODO 200, 400 bad req, 401 forbidden, 403 taken, 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        authDAO.addAuthToken(token);
        service = new JoinGameService(authDAO, gameDAO);
    }

    // Positive test
    @Test
    void join_Game_returns_okay() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHITE", 4), token);
        // TODO test
    }

    // Negative test
    @Test
    void join_nonexistent_Game_returns_bad_request_error() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHITE", 4), token);
        // TODO test
    }

    @Test
    void join_Game_without_color_returns_okay() throws DataAccessException {
        service.joinGame(new JoinGameRequest(null, 4), token);
        // TODO test
    }

    @Test
    void join_Game_with_invalid_token_errors() throws DataAccessException {
        service.joinGame(new JoinGameRequest("WHITE", 4), token);
        // TODO test
    }

}