package dataAccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import server.AuthToken;
import server.User;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthDAOTest {
    AuthDAO authDAO;
    UserDAO userDAO;

    // TODO Change to parameterized tests
    // TODO? Add null or empty string tests?

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = new MemoryUserDAO();
        User user1 = new User("user1", "pass1", "mail1");
        User user2 = new User("user2", "pass2", "mail2");
        userDAO.insertNewUser(user1);
        userDAO.insertNewUser(user2);

        authDAO = new MemoryAuthDAO(userDAO);
    }

    @Test
    void addAuthTokenDoesNotError() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token);
        assertTrue(true);
    }

    @Test
    void addAuthTokenNonexistentUserErrors() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "idontexist");
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> authDAO.addAuthToken(token));
    }

    @Test
    void addSameTokenTwiceErrors() throws DataAccessException {
        AuthToken token1 = new AuthToken("1234", "user1");
        AuthToken token2 = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token1);

        Assertions.assertThrows(ValueAlreadyTakenException.class, () -> authDAO.addAuthToken(token2));
    }

    @Test
    @Disabled
        // Only applies using AuthTokens, not when just using Strings
    void addSameTokenStringDifferentUsersDoesNotError() throws DataAccessException {
        AuthToken token1 = new AuthToken("1234", "user1");
        AuthToken token2 = new AuthToken("1234", "user2");
        authDAO.addAuthToken(token1);
        authDAO.addAuthToken(token2);
        Assertions.assertTrue(true);
    }

    @Test
    void addDifferentTokenStringSameUserDoesNotError() throws DataAccessException {
        AuthToken token1 = new AuthToken("12345678", "user1");
        AuthToken token2 = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token1);
        authDAO.addAuthToken(token2);
        Assertions.assertTrue(true);
    }

    @Test
    void addedTokenIsValid() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token);
        Assertions.assertTrue(authDAO.isValidAuthToken(token.authToken()));
    }

    @Test
    void removedTokenIsNotValid() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        authDAO.addAuthToken(token);
        authDAO.removeAuthToken(token.authToken());
        Assertions.assertFalse(authDAO.isValidAuthToken(token.authToken()));
    }

    @Test
    void removingNonexistentTokenIsOkay() throws DataAccessException {
        AuthToken token = new AuthToken("1234", "user1");
        authDAO.removeAuthToken(token.authToken());
        Assertions.assertTrue(true);
    }

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
}








