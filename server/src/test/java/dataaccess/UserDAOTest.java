package dataaccess;

import dataaccess.exception.DataAccessException;
import dataaccess.exception.ValueAlreadyTakenException;
import dataaccess.memory.MemoryUserDAO;
import dataaccess.sql.DatabaseUserDAO;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private static final boolean IS_SQL_DAO = true;
    private static final User USER = new User("validUser", "password", "email");
    private static UserDAO userDAO;

    @AfterAll
    static void deInit() throws DataAccessException {
        userDAO.clearUsers();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO = IS_SQL_DAO ? new DatabaseUserDAO() : new MemoryUserDAO();
        userDAO.clearUsers();
    }

    @Test
    void getInsertedUserHasCorrectUsername() throws DataAccessException {
        userDAO.insertNewUser(USER);
        User retrievedUser = userDAO.getUser(USER.username());
        assertEquals(retrievedUser.username(), USER.username());
    }

    @Test
    void hasUserOnInsertedUser() throws DataAccessException {
        userDAO.insertNewUser(USER);
        assertTrue(userDAO.hasUser(USER.username()));
    }

    @Test
    void insertExistingUserThrows() throws DataAccessException {
        userDAO.insertNewUser(USER);
        assertThrows(ValueAlreadyTakenException.class, () -> userDAO.insertNewUser(USER));
    }

    @Test
    void hasUserOnNonexistentUser() throws DataAccessException {
        assertFalse(userDAO.hasUser("invalidUsername"));
    }

    @Test
    void hasUserOnRemovedUser() throws DataAccessException {
        userDAO.insertNewUser(USER);
        userDAO.removeUser(USER);
        assertFalse(userDAO.hasUser("invalidUsername"));
    }

    @Test
    void removeNonexistentUserDoesNotThrow() throws DataAccessException {
        userDAO.removeUser(USER);
    }

    @Test
    void hasUserOnClearedUsers() throws DataAccessException {
        User user2 = new User("username2", "pass2", "email2");
        userDAO.insertNewUser(USER);
        userDAO.insertNewUser(user2);
        userDAO.clearUsers();
        assertFalse(userDAO.hasUser(USER.username()));
        assertFalse(userDAO.hasUser(user2.username()));
    }

    @Test
    void clearUsersWhenEmptyDoesNotThrow() throws DataAccessException {
        userDAO.clearUsers();
    }
}
