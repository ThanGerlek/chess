package dataaccess;

import model.AuthToken;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {
    private static final boolean IS_SQL_DAO = true;
    private static final User validUser = new User("validUser", "password", "email");
    private static final AuthToken validToken = new AuthToken("validTokenString", validUser.username());
    private static UserDAO userDAO;
    private static AuthDAO authDAO;

    @BeforeAll
    static void init() throws DataAccessException {
        userDAO = new MemoryUserDAO();
        userDAO.clearUsers();
        userDAO.insertNewUser(validUser);
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
        authDAO.addAuthToken(validToken);
        assertTrue(authDAO.isValidAuthToken(validToken.authToken()));
    }

    @Test
    void addExistingAuthTokenThrows() throws DataAccessException {
        authDAO.addAuthToken(validToken);
        assertThrows(ValueAlreadyTakenException.class, () -> authDAO.addAuthToken(validToken));
    }

    @Test
    void addAuthTokenWithIncorrectUsernameThrows() {
        AuthToken invalidToken = new AuthToken(validToken.authToken(), "invalidUsername");
        assertThrows(UnauthorizedAccessException.class, () -> authDAO.addAuthToken(invalidToken));
    }

    @Test
    void nonexistentTokenIsInvalid() throws DataAccessException {
        AuthToken invalidToken = new AuthToken("invalidTokenString", validUser.username());
        assertFalse(authDAO.isValidAuthToken(invalidToken.authToken()));
    }

    @Test
    void removeAuthTokenMakesTokenInvalid() throws DataAccessException {
        authDAO.addAuthToken(validToken);
        authDAO.removeAuthToken(validToken.authToken());
        assertFalse(authDAO.isValidAuthToken(validToken.authToken()));
    }

    @Test
    void removeNonexistentAuthTokenDoesNotThrow() throws DataAccessException {
        authDAO.removeAuthToken("invalidAuthToken");
    }

    @Test
    void clearAuthTokensMakesTokensInvalid() throws DataAccessException {
        authDAO.addAuthToken(validToken);
        AuthToken validToken2 = new AuthToken("tokenString2", validUser.username());
        authDAO.addAuthToken(validToken2);

        authDAO.clearAuthTokens();
        assertFalse(authDAO.isValidAuthToken(validToken.authToken()));
        assertFalse(authDAO.isValidAuthToken(validToken2.authToken()));
    }

    @Test
    void clearAuthTokensWhenEmptyDoesNotThrow() throws DataAccessException {
        authDAO.clearAuthTokens();
    }

    @Test
    void getUsernameReturnsCorrectUsername() throws DataAccessException {
        authDAO.addAuthToken(validToken);
        assertEquals(validUser.username(), authDAO.getUsername(validToken.authToken()));
    }

    @Test
    void getUsernameOfNonexistentTokenThrows() {
        assertThrows(UnauthorizedAccessException.class, () -> authDAO.getUsername("invalidTokenString"));
    }
}
