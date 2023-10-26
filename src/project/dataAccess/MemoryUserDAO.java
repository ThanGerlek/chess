package dataAccess;

import server.User;

import java.util.ArrayList;

/**
 * A DAO (Data Access Object) for CRUD operations on the list of registered Users.
 */
public class MemoryUserDAO implements UserDAO {

    private static ArrayList<User> userDatabase = new ArrayList<>();

    /**
     * Adds a new User to the database.
     *
     * @param user the User to insert
     * @throws DataAccessException if the username is already in the database
     */
    public void insertNewUser(User user) throws DataAccessException {
        /* Failures
        can't access database
        username already exists
        */
        // TODO
    }

    /**
     * Gets the User with the given username from the database.
     *
     * @param username the username of the User to fetch
     * @return the User
     * @throws DataAccessException if the User was not found
     */
    public User getUser(String username) throws DataAccessException {
        /* Failures
        can't access database
        user not found
        */
        // TODO
        return null;
    }

    /**
     * Returns true if a User with the given username exists in the database.
     *
     * @param username the username of the User to fetch
     * @return true if the User was found, false otherwise
     */
    public boolean hasUser(String username) throws DataAccessException {
        /* Failures
        can't access database
        */
        // TODO
        return false;
    }

    /**
     * Removes a single User from the database.
     *
     * @param user the User to remove
     */
    public void removeUser(User user) throws DataAccessException {
        /* Failures
        can't access database
        (if user DNE, just return)
        */
        // TODO
    }

    /**
     * Removes every User from the database.
     */
    public void clearUsers() throws DataAccessException {
        /* Failures
        can't access database
        (if no users, just return)
        */
        // TODO
    }
}
