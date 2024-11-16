package dataaccess;

import dataaccess.exception.DataAccessException;
import dataaccess.exception.UnauthorizedAccessException;
import dataaccess.exception.ValueAlreadyTakenException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import dataaccess.sql.DatabaseAuthDAO;
import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {
    private static final boolean IS_SQL_DAO = true;
    private static final User VALID_USER = new User("validUser", "password", "email");
    private static final AuthToken VALID_TOKEN = new AuthToken("validTokenString", VALID_USER.username());
    private static UserDAO userDAO;
    private static AuthDAO authDAO;

    @BeforeAll
    static void init() throws DataAccessException {
        userDAO = new MemoryUserDAO();
        userDAO.clearUsers();
        userDAO.insertNewUser(VALID_USER);
    }

    @AfterAll
    static void deInit() throws DataAccessException {
        userDAO.clearUsers();
        authDAO.clearAuthTokens();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO = IS_SQL_DAO ? new DatabaseAuthDAO(userDAO) : new MemoryAuthDAO(userDAO);
        authDAO.clearAuthTokens();
    }

    @Test
    void addNewAuthTokenMakesTokenValid() throws DataAccessException {
        authDAO.addAuthToken(VALID_TOKEN);
        assertTrue(authDAO.isValidAuthToken(VALID_TOKEN.authToken()));
    }

    @Test
    void addExistingAuthTokenThrows() throws DataAccessException {
        authDAO.addAuthToken(VALID_TOKEN);
        assertThrows(ValueAlreadyTakenException.class, () -> authDAO.addAuthToken(VALID_TOKEN));
    }

    @Test
    void addAuthTokenWithIncorrectUsernameThrows() {
        AuthToken invalidToken = new AuthToken(VALID_TOKEN.authToken(), "invalidUsername");
        assertThrows(UnauthorizedAccessException.class, () -> authDAO.addAuthToken(invalidToken));
    }

    @Test
    void nonexistentTokenIsInvalid() throws DataAccessException {
        AuthToken invalidToken = new AuthToken("invalidTokenString", VALID_USER.username());
        assertFalse(authDAO.isValidAuthToken(invalidToken.authToken()));
    }

    @Test
    void removeAuthTokenMakesTokenInvalid() throws DataAccessException {
        authDAO.addAuthToken(VALID_TOKEN);
        authDAO.removeAuthToken(VALID_TOKEN.authToken());
        assertFalse(authDAO.isValidAuthToken(VALID_TOKEN.authToken()));
    }

    @Test
    void removeNonexistentAuthTokenDoesNotThrow() throws DataAccessException {
        authDAO.removeAuthToken("invalidAuthToken");
    }

    @Test
    void clearAuthTokensMakesTokensInvalid() throws DataAccessException {
        authDAO.addAuthToken(VALID_TOKEN);
        AuthToken validToken2 = new AuthToken("tokenString2", VALID_USER.username());
        authDAO.addAuthToken(validToken2);

        authDAO.clearAuthTokens();
        assertFalse(authDAO.isValidAuthToken(VALID_TOKEN.authToken()));
        assertFalse(authDAO.isValidAuthToken(validToken2.authToken()));
    }

    @Test
    void clearAuthTokensWhenEmptyDoesNotThrow() throws DataAccessException {
        authDAO.clearAuthTokens();
    }

    @Test
    void getUsernameReturnsCorrectUsername() throws DataAccessException {
        authDAO.addAuthToken(VALID_TOKEN);
        assertEquals(VALID_USER.username(), authDAO.getUsername(VALID_TOKEN.authToken()));
    }

    @Test
    void getUsernameOfNonexistentTokenThrows() {
        assertThrows(UnauthorizedAccessException.class, () -> authDAO.getUsername("invalidTokenString"));
    }
}
