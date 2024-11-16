package dataaccess;

import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private static final boolean IS_SQL_DAO = true;
    private static final User user = new User("validUser", "password", "email");
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
        userDAO.insertNewUser(user);
        User retrievedUser = userDAO.getUser(user.username());
        assertEquals(retrievedUser.username(), user.username());
    }

    @Test
    void hasUserOnInsertedUser() throws DataAccessException {
        userDAO.insertNewUser(user);
        assertTrue(userDAO.hasUser(user.username()));
    }

    @Test
    void insertExistingUserThrows() throws DataAccessException {
        userDAO.insertNewUser(user);
        assertThrows(ValueAlreadyTakenException.class, () -> userDAO.insertNewUser(user));
    }

    @Test
    void hasUserOnNonexistentUser() throws DataAccessException {
        assertFalse(userDAO.hasUser("invalidUsername"));
    }

    @Test
    void hasUserOnRemovedUser() throws DataAccessException {
        userDAO.insertNewUser(user);
        userDAO.removeUser(user);
        assertFalse(userDAO.hasUser("invalidUsername"));
    }

    @Test
    void removeNonexistentUserDoesNotThrow() throws DataAccessException {
        userDAO.removeUser(user);
    }

    @Test
    void hasUserOnClearedUsers() throws DataAccessException {
        User user2 = new User("username2", "pass2", "email2");
        userDAO.insertNewUser(user);
        userDAO.insertNewUser(user2);
        userDAO.clearUsers();
        assertFalse(userDAO.hasUser(user.username()));
        assertFalse(userDAO.hasUser(user2.username()));
    }

    @Test
    void clearUsersWhenEmptyDoesNotThrow() throws DataAccessException {
        userDAO.clearUsers();
    }
}
