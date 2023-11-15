package dataAccess;

import model.AuthToken;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthDAOTest {
    private final boolean USE_DATABASE_DAOS = true;
    private final ChessDatabase database = new ChessDatabase();
    private AuthDAO authDAO;
    private UserDAO userDAO;

    // TODO Change to parameterized tests
    // TODO? Add null or empty string tests?

    @BeforeEach
    void setUp() throws DataAccessException {
        database.update("TRUNCATE users");
        database.update("TRUNCATE auth");
        userDAO = (USE_DATABASE_DAOS) ? new DatabaseUserDAO(database) : new MemoryUserDAO();
        authDAO = (USE_DATABASE_DAOS) ? new DatabaseAuthDAO(database, userDAO) : new MemoryAuthDAO(userDAO);
        User user1 = new User("user1", "pass1", "mail1");
        User user2 = new User("user2", "pass2", "mail2");
        userDAO.insertNewUser(user1);
        userDAO.insertNewUser(user2);
    }

    @Test
    void addAuthTokenDoesNotError() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        Assertions.assertDoesNotThrow(() -> {
            authDAO.addAuthToken(token);
        });
    }

    @Test
    void addAuthTokenNonexistentUserErrors() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "idontexist");
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> authDAO.addAuthToken(token));
    }

    // addAuthToken negative test
    @Test
    void addSameTokenTwiceErrors() throws DataAccessException {
        AuthToken token1 = new AuthToken("1234", "user1");
        AuthToken token2 = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token1);

        Assertions.assertThrows(ValueAlreadyTakenException.class, () -> authDAO.addAuthToken(token2));
    }

    @Test
    void addDifferentTokenStringSameUserDoesNotError() throws DataAccessException {
        AuthToken token1 = new AuthToken("12345678", "user1");
        AuthToken token2 = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token1);

        Assertions.assertDoesNotThrow(() -> {
            authDAO.addAuthToken(token2);
        });
    }

    // addAuthToken positive test
    // isValidToken positive test
    @Test
    void addedTokenIsValid() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token);
        Assertions.assertTrue(authDAO.isValidAuthToken(token.authToken()));
    }

    // isValidToken negative test
    @Test
    void nonexistentTokenIsNotValid() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token);
        Assertions.assertTrue(authDAO.isValidAuthToken(token.authToken()));
    }

    // removeAuthToken positive test
    @Test
    void removedTokenIsNotValid() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token);
        authDAO.removeAuthToken(token.authToken());
        Assertions.assertFalse(authDAO.isValidAuthToken(token.authToken()));
    }

    // removeAuthToken "negative" test
    @Test
    void removingNonexistentTokenDoesNotThrow() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        Assertions.assertDoesNotThrow(() -> {
            authDAO.removeAuthToken(token.authToken());
        });
    }

    // clearAuthTokens positive test
    @Test
    void clearedAuthTokensAreInvalid() throws DataAccessException {
        AuthToken token1 = new AuthToken("1234", "user1");
        AuthToken token2 = new AuthToken("5678", "user2");
        authDAO.addAuthToken(token1);
        authDAO.addAuthToken(token2);

        authDAO.clearAuthTokens();

        Assertions.assertFalse(authDAO.isValidAuthToken(token1.authToken()));
        Assertions.assertFalse(authDAO.isValidAuthToken(token2.authToken()));
    }

    // getUsername positive test
    @Test
    void getUsernameReturnsUsername() throws DataAccessException {
        authDAO.addAuthToken(new AuthToken("1234", "user1"));
        Assertions.assertEquals("user1", authDAO.getUsername("1234"));
    }

    // getUsername negative test
    @Test
    void getUsernameOfInvalidTokenErrors() throws DataAccessException {
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {
            String username = authDAO.getUsername("iAmInvalid");
        });
    }
}








