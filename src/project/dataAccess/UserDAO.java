package dataAccess;

import models.User;

/**
 * A DAO (Data Access Object) for CRUD operations on the list of registered users.
 */
public class UserDAO {

    /**
     * Adds a new {@link models.User} to the database.
     *
     * @param user the user to insert
     * @throws DataAccessException if the username is already in the database
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void insertNewUser(User user) throws DataAccessException {
        /* Failures
        can't access database
        username already exists
        */
    }

    /**
     * Gets the {@link models.User} with the given username from the database.
     *
     * @param username the username of the user to fetch
     * @return the fetched {@code User}
     * @throws DataAccessException if the user was not found
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public User getUser(String username) throws DataAccessException {
        /* Failures
        can't access database
        user not found
        */
        return null;
    }

    /**
     * Removes a single user from the database.
     *
     * @param user the user to remove
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void removeUser(User user) throws DataAccessException {
        /* Failures
        can't access database
        (if user DNE, just return)
        */
    }

    /**
     * Removes every user from the database.
     *
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void clearUsers() throws DataAccessException {
        /* Failures
        can't access database
        (if no users, just return)
        */
    }
}
