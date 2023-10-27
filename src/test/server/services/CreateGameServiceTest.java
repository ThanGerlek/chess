package server.services;

import dataAccess.DataAccessException;
import dataAccess.UnauthorizedAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.User;
import server.http.CreateGameRequest;
import server.http.CreateGameResponse;

class CreateGameServiceTest extends ServiceTest {
    private final User user = new User("user1", "pass1", "mail1");
    private final AuthToken token = new AuthToken("1234", "user1");
    private final CreateGameRequest request = new CreateGameRequest("game1");
    private CreateGameService service;

    // TODO 400 bad req, 401 forbidden, 500?

    @BeforeEach
    void setUp() throws DataAccessException {
        initDAOs();
        userDAO.insertNewUser(user);
        authDAO.addAuthToken(token);
        service = new CreateGameService(authDAO, gameDAO);
    }

    // Positive test
    @Test
    void create_Game_returns_okay() throws DataAccessException {
        CreateGameResponse response = service.createGame(request, token);
//        Assertions.assertEquals(200, response.status());
        // TODO Assert something
    }

    // Negative test
    @Test
    void create_Game_with_invalid_token_returns_forbidden() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedAccessException.class,
                () -> service.createGame(request, new AuthToken("1234", "iDoNotExist")));
    }

}