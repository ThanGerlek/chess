package server.services;

import chess.ChessGameImpl;
import dataAccess.DataAccessException;
import dataAccess.NoSuchItemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.Game;
import server.User;
import server.http.JoinGameRequest;
import server.http.MessageResponse;

class JoinGameServiceTest extends ServiceTest {
    private static final int INVALID_GAME_ID = 42;
    private final User user = new User("user1", "pass1", "mail1");
    private final AuthToken token = new AuthToken("1234", "user1");
    private final JoinGameRequest request = new JoinGameRequest("WHITE", 1);
    private JoinGameService service;

    // TODO 401 forbidden, 403 taken, 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        userDAO.insertNewUser(user);
        authDAO.addAuthToken(token);
        gameDAO.insertNewGame(new Game(1, "game1", new ChessGameImpl()));
        service = new JoinGameService(authDAO, gameDAO);
    }

    // Positive test
    @Test
    void join_Game_returns_okay() throws DataAccessException {
        MessageResponse response = service.joinGame(request, token);
//        Assertions.assertEquals(200, response.status());
        // TODO Assert something
    }

    // Negative test
    @Test
    void join_nonexistent_Game_returns_bad_request_error() throws DataAccessException {
        Assertions.assertThrows(NoSuchItemException.class,
                () -> service.joinGame(new JoinGameRequest("WHITE", INVALID_GAME_ID), token));
    }

    @Test
    @Disabled
        // TODO test
    void join_Game_without_color_returns_okay() throws DataAccessException {
        service.joinGame(new JoinGameRequest(null, 1), token);
    }

    @Test
    @Disabled
        // TODO test
    void join_Game_with_invalid_token_errors() throws DataAccessException {
        service.joinGame(request, new AuthToken("iAmIncorrect", "user1"));
    }

}