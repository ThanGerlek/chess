package service;

import dataaccess.exception.DataAccessException;
import dataaccess.exception.NoSuchItemException;
import model.AuthToken;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.ClearApplicationService;

class ClearApplicationServiceTest extends ServiceTest {
    private ClearApplicationService service;

    // TODO 500?

    @BeforeEach
    void setUp() {
        initDAOs();
        service = new ClearApplicationService(authDAO, gameDAO, userDAO, null);
    }

    // Positive test
    @Test
    void hasClearedUsersIsFalse() throws DataAccessException {
        userDAO.insertNewUser(new User("user1", "pass1", "mail1"));
        userDAO.insertNewUser(new User("user2", "pass2", "mail2"));

        service.clearApplication();

        Assertions.assertFalse(userDAO.hasUser("user1"));
        Assertions.assertFalse(userDAO.hasUser("user2"));
    }

    @Test
    void clearingDoesNotThrow() {
        Assertions.assertDoesNotThrow(() -> {
            service.clearApplication();
        });
    }

    @Test
    void clearingTwiceDoesNotThrow() throws DataAccessException {
        service.clearApplication();
        Assertions.assertDoesNotThrow(() -> {
            service.clearApplication();
        });
    }

    @Test
    void findingClearedGamesErrors() throws DataAccessException {
        gameDAO.insertNewGame(new Game(1, "game1"));
        gameDAO.insertNewGame(new Game(2, "game2"));

        service.clearApplication();

        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(1));
        Assertions.assertThrows(NoSuchItemException.class, () -> gameDAO.findGame(2));
    }

    @Test
    void clearedAuthTokensAreInvalid() throws DataAccessException {
        AuthToken token1 = new AuthToken("1234", "user1");
        AuthToken token2 = new AuthToken("2468", "user2");
        userDAO.insertNewUser(new User("user1", "pass1", "mail1"));
        userDAO.insertNewUser(new User("user2", "pass2", "mail2"));
        authDAO.addAuthToken(token1);
        authDAO.addAuthToken(token2);

        service.clearApplication();

        Assertions.assertFalse(authDAO.isValidAuthToken(token1.authToken()));
        Assertions.assertFalse(authDAO.isValidAuthToken(token2.authToken()));
    }
}
