package dataAccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.User;

class UserDAOTest {
    private final boolean USE_DATABASE_DAOS = true;
    private UserDAO userDAO;

    private ChessDatabase database = new ChessDatabase();

    @BeforeEach
    void setUp() throws DataAccessException {
        database.update("TRUNCATE users");
        userDAO = USE_DATABASE_DAOS ? new DatabaseUserDAO(database) : new MemoryUserDAO();
    }

    @Test
    void insertUserDoesNotError() throws DataAccessException {
        User user = new User("user1", "pass1", "mail1");
        userDAO.insertNewUser(user);
        Assertions.assertTrue(true);
    }

    @Test
    void insertUsersWithSameUsernameErrors() throws DataAccessException {
        User user1 = new User("user", "pass1", "mail1");
        User user2 = new User("user", "pass2", "mail2");
        userDAO.insertNewUser(user1);

        Assertions.assertThrows(ValueAlreadyTakenException.class, () -> userDAO.insertNewUser(user2));
    }

    @Test
    void getInsertedUserReturnsUser() throws DataAccessException {
        User user = new User("user1", "pass1", "mail1");
        userDAO.insertNewUser(user);

        User fetchedUser = userDAO.getUser("user1");
        Assertions.assertEquals(user, fetchedUser);
    }

    @Test
    void getInvalidUser_throws_not_found_error() {
        Assertions.assertThrows(NoSuchItemException.class, () -> userDAO.getUser("iDoNotExist"));
    }

    @Test
    void hasInsertedUserReturnsTrue() throws DataAccessException {
        User user = new User("user1", "pass1", "mail1");
        userDAO.insertNewUser(user);
        Assertions.assertTrue(userDAO.hasUser("user1"));
    }

    @Test
    void hasNonexistentUserReturnsFalse() throws DataAccessException {
        Assertions.assertFalse(userDAO.hasUser("iDoNotExist"));
    }

    @Test
    void hasRemovedUserReturnsFalse() throws DataAccessException {
        User user = new User("user1", "pass1", "mail1");
        userDAO.insertNewUser(user);
        userDAO.removeUser(user);
        Assertions.assertFalse(userDAO.hasUser("user1"));
    }

    @Test
    void hasClearedUsersReturnsFalse() throws DataAccessException {
        User user1 = new User("user1", "pass1", "mail1");
        User user2 = new User("user2", "pass2", "mail2");
        userDAO.insertNewUser(user1);
        userDAO.insertNewUser(user2);
        userDAO.clearUsers();
        Assertions.assertFalse(userDAO.hasUser("user1"));
        Assertions.assertFalse(userDAO.hasUser("user2"));
    }

    @Test
    void clearEmptyIsOkay() throws DataAccessException {
        userDAO.clearUsers();
        Assertions.assertTrue(true);
    }
}