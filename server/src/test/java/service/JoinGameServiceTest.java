package service;

import dataaccess.*;
import dataaccess.exception.*;
import http.JoinGameRequest;
import http.MessageResponse;
import model.AuthToken;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.JoinGameService;

class JoinGameServiceTest extends ServiceTest {
    private static final int INVALID_GAME_ID = 42;
    private final User user1 = createTestUser("user1", "pass1", "mail1");
    private final User user2 = createTestUser("user2", "pass2", "mail2");
    private final AuthToken token1 = new AuthToken("1234", "user1");
    private final AuthToken token2 = new AuthToken("5678", "user2");
    private final JoinGameRequest requestW = new JoinGameRequest(PlayerRole.roleToString(PlayerRole.WHITE_PLAYER), 1);
    private final JoinGameRequest requestB = new JoinGameRequest(PlayerRole.roleToString(PlayerRole.BLACK_PLAYER), 1);
    private JoinGameService service;

    // TODO 401 forbidden, 403 taken, 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        userDAO.insertNewUser(user1);
        authDAO.addAuthToken(token1);
        userDAO.insertNewUser(user2);
        authDAO.addAuthToken(token2);
        gameDAO.insertNewGame(new Game(1, "game1"));
        service = new JoinGameService(authDAO, gameDAO);
    }

    // Positive test
    @Test
    void joinGameAsWhiteAddsUserAsWhite() throws DataAccessException {
        service.joinGame(requestW, token1.authToken());
        Assertions.assertEquals("user1", gameDAO.findGame(1).whiteUsername());
    }

    @Test
    void joinGameAsBlackAddsUserAsBlack() throws DataAccessException {
        service.joinGame(requestB, token1.authToken());
        Assertions.assertEquals("user1", gameDAO.findGame(1).blackUsername());
    }

    // Negative test
    @Test
    void joinNonexistentGameReturnsBadRequestError() {
        Assertions.assertThrows(NoSuchItemException.class,
                () -> service.joinGame(
                        new JoinGameRequest(PlayerRole.roleToString(PlayerRole.WHITE_PLAYER), INVALID_GAME_ID),
                        token1.authToken())
        );
    }

    @Test
    void joinGameReturnsOkay() throws DataAccessException {
        MessageResponse response = service.joinGame(requestW, token1.authToken());
        Assertions.assertEquals("Okay!", response.message());
    }

    @Test
    void joinGameWithoutColorThrowsBadRequestException() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            service.joinGame(new JoinGameRequest(null, 1), token1.authToken());
        });
    }

    @Test
    void joinGameWithAlreadyTakenColorReturnsTaken() throws DataAccessException {
        service.joinGame(requestW, token1.authToken());

        Assertions.assertThrows(ValueAlreadyTakenException.class, () -> service.joinGame(requestW, token2.authToken()));
    }

    @Test
    void joinGameWithSameColorTwiceReturnsOkay() throws DataAccessException {
        service.joinGame(requestW, token1.authToken());

        MessageResponse response = service.joinGame(requestW, token1.authToken());

        Assertions.assertEquals("Okay!", response.message());
    }

    @Test
    void joinGameWithInvalidTokenErrors() {
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> service.joinGame(requestW, "iAmIncorrect"));
    }

}
