package client.httpConnection;

import chess.ChessGame;
import http.AuthResponse;
import http.GameListItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ChessServerFacadeTest {
    private static final String DEFAULT_SERVER_URL = "http://localhost:8080"; // TODO. Pull from config file
    private static ChessServerFacade facade;
    int nextTestUserID = 0;
    int nextTestGameNameID = 0;

    // TODO More thorough testing
    //  More specific Exception types

    @AfterAll
    static void tearDown() throws FailedConnectionException, FailedResponseException {
        facade.clearApplication();
    }

    @BeforeEach
    void setUp() throws FailedConnectionException, FailedResponseException {
        facade = new ChessServerFacade(DEFAULT_SERVER_URL);
        facade.clearApplication();
        // TODO Check for race conditions?
    }

    @Test
    void testTheTest__isValidAuthTokenString_of_generateValidAuthTokenString_returns_true()
            throws FailedConnectionException, FailedResponseException {
        String authTokenString = generateValidAuthTokenString();
        Assertions.assertTrue(isValidAuthTokenString(authTokenString));
    }

    private String generateValidAuthTokenString() throws FailedConnectionException, FailedResponseException {
        // TODO Make independent of register()? (Add test function to ServerFacade?)
        String username = generateTestUsername();
        AuthResponse response = facade.register(username, "password", "email");
        return response.authToken();
    }

    private boolean isValidAuthTokenString(String authTokenString) {
        // TODO Make independent of listGames()? (Add test function to ServerFacade?)
        try {
            facade.listGames(authTokenString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateTestUsername() {
        // TODO? Replace with registerTestUser that returns a User model object?
        nextTestUserID++;
        return String.format("testUser_%d", nextTestUserID);
    }

    @Test
    void testTheTest__isValidAuthTokenString_of_invalid_returns_false() {
        Assertions.assertFalse(isValidAuthTokenString("iAmInvalid"));
    }

    @Test
    void generateTestUsername_twice_returns_different_usernames() {
        Assertions.assertNotEquals(generateTestUsername(), generateTestUsername());
    }

    @Test
    void generateTestGameName_twice_returns_different_gameNames() {
        Assertions.assertNotEquals(generateTestGameName(), generateTestGameName());
    }

    private String generateTestGameName() {
        nextTestGameNameID++;
        return String.format("testGameName_%d", nextTestGameNameID);
    }

    @Test
    void clearApplication_invalidates_token() throws FailedConnectionException, FailedResponseException {
        // clearApplication() positive test
        String authTokenString = generateValidAuthTokenString();
        facade.clearApplication();
        Assertions.assertFalse(isValidAuthTokenString(authTokenString));
    }

    @Test
    void login_as_cleared_user_throws_failedResponseException()
            throws FailedConnectionException, FailedResponseException {
        String username = generateTestUsername();
        facade.register(username, "password", "");

        facade.clearApplication();

        Assertions.assertThrows(FailedResponseException.class, () -> {
            facade.login(username, "password");
        });
    }

    @Test
    void listGames_after_clearApplication_returns_empty() throws FailedConnectionException, FailedResponseException {
        String authTokenString = generateValidAuthTokenString();
        facade.createGame(generateTestGameName(), authTokenString);

        facade.clearApplication();

        authTokenString = generateValidAuthTokenString();
        ArrayList<GameListItem> games = facade.listGames(authTokenString);
        Assertions.assertEquals(0, games.size());
    }

    @Test
    void register_new_user_returns_valid_authTokenString() throws FailedConnectionException, FailedResponseException {
        // register() positive test
        String username = generateTestUsername();
        AuthResponse response = facade.register(username, "password", "email");
        Assertions.assertTrue(isValidAuthTokenString(response.authToken()));
    }

    @Test
    void register_user_twice_throws_failedResponseException()
            throws FailedConnectionException, FailedResponseException {
        // register() negative test
        String username = generateTestUsername();
        facade.register(username, "password", "email");

        Assertions.assertThrows(FailedResponseException.class, () -> {
            facade.register(username, "password", "email");
        });
    }

    @Test
    void login_valid_user_returns_valid_authTokenString() throws FailedConnectionException, FailedResponseException {
        // login() positive test
        String username = generateTestUsername();
        facade.register(username, "password", "email");

        AuthResponse response = facade.login(username, "password");

        Assertions.assertTrue(isValidAuthTokenString(response.authToken()));
    }

    @Test
    void login_incorrect_password_throws_failedResponseException()
            throws FailedConnectionException, FailedResponseException {
        // login() negative test
        String username = generateTestUsername();
        facade.register(username, "password", "email");

        Assertions.assertThrows(FailedResponseException.class, () -> {
            facade.login(username, "iAmIncorrect");
        });
    }

    @Test
    void logout_current_user_invalidates_token() throws FailedConnectionException, FailedResponseException {
        // logout() positive test
        String username = generateTestUsername();
        AuthResponse response = facade.register(username, "password", "email");
        String authTokenString = response.authToken();

        facade.logout(authTokenString);

        Assertions.assertFalse(isValidAuthTokenString(authTokenString));
    }

    @Test
    void logout_nonexistent_authTokenString_throws_failedResponseException() {
        // logout() negative test
        Assertions.assertThrows(FailedResponseException.class, () -> {
            facade.logout("iAmInvalid");
        });
    }

    @Test
    void valid_call_to_createGame_adds_game_to_listGames() throws FailedConnectionException, FailedResponseException {
        // createGame() positive test
        String authTokenString = generateValidAuthTokenString();
        String gameName = generateTestGameName();

        facade.createGame(gameName, authTokenString);

        // TODO Race condition?
        ArrayList<GameListItem> gameList = facade.listGames(authTokenString);
        Assertions.assertTrue(containsGameName(gameList, gameName));
    }

    private boolean containsGameName(ArrayList<GameListItem> gameList, String gameName) {
        return getIndexFromGameName(gameList, gameName) >= 0;
    }

    private int getIndexFromGameName(ArrayList<GameListItem> gameList, String gameName) {
        for (int i = 0; i < gameList.size(); i++) {
            GameListItem game = gameList.get(i);
            if (gameName.equals(game.gameName())) {
                return i;
            }
        }
        return -1;
    }

    @Test
    void createGame_with_invalid_token_throws_failedResponseException() {
        // createGame() negative test
        Assertions.assertThrows(FailedResponseException.class, () -> {
            String gameName = generateTestGameName();
            facade.createGame(gameName, "iAmAnInvalidAuthTokenString");
        });
    }

    @Test
    void listGames_with_two_active_games_returns_array_containing_both_gameNames()
        // listGames() positive test
            throws FailedConnectionException, FailedResponseException {
        String authTokenString = generateValidAuthTokenString();
        String gameName1 = generateTestGameName();
        String gameName2 = generateTestGameName();
        facade.createGame(gameName1, authTokenString);
        facade.createGame(gameName2, authTokenString);

        ArrayList<GameListItem> games = facade.listGames(authTokenString);

        Assertions.assertTrue(containsGameName(games, gameName1));
        Assertions.assertTrue(containsGameName(games, gameName2));
    }

    @Test
    void listGames_with_no_active_games_returns_empty() throws FailedConnectionException, FailedResponseException {
        // listGames() negative test
        // TODO
        String authTokenString = generateValidAuthTokenString();
        ArrayList<GameListItem> games = facade.listGames(authTokenString);
        Assertions.assertEquals(0, games.size());
    }

    @Test
    void joinGame_as_white_adds_player_as_white_in_listGames()
            throws FailedConnectionException, FailedResponseException {
        // joinGame() positive test
        String username = generateTestUsername();
        String authTokenString = facade.register(username, "password", "").authToken();
        String gameName = generateTestGameName();
        int gameID = facade.createGame(gameName, authTokenString);

        facade.joinGame(ChessGame.TeamColor.WHITE, gameID, authTokenString);

        ArrayList<GameListItem> games = facade.listGames(authTokenString);
        int gameIndex = getIndexFromGameName(games, gameName);
        GameListItem gameListItem = games.get(gameIndex);
        Assertions.assertEquals(username, gameListItem.whiteUsername());
    }

    @Test
    void joinGame_as_white_when_already_taken_throws_failedResponseException()
            throws FailedConnectionException, FailedResponseException {
        // joinGame() negative test
        String username1 = generateTestUsername();
        String authTokenString1 = facade.register(username1, "password1", "").authToken();
        String username2 = generateTestUsername();
        String authTokenString2 = facade.register(username2, "password2", "").authToken();

        String gameName = generateTestGameName();
        int gameID = facade.createGame(gameName, authTokenString1);

        facade.joinGame(ChessGame.TeamColor.WHITE, gameID, authTokenString1);

        // TODO Race condition?
        Assertions.assertThrows(FailedResponseException.class, () -> {
            facade.joinGame(ChessGame.TeamColor.WHITE, gameID, authTokenString2);
        });
    }
}