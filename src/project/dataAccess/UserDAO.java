package dataAccess;

import server.User;

/**
 * A DAO (Data Access Object) for CRUD operations on the list of registered Users.
 */
public interface UserDAO {

    /**
     * Adds a new User to the database.
     *
     * @param user the User to insert
     * @throws DataAccessException if the username is already in the database
     */
    void insertNewUser(User user) throws DataAccessException;
        /* Failures
        can't access database
        username already exists
        */

    /**
     * Gets the User with the given username from the database.
     *
     * @param username the username of the User to fetch
     * @return the User
     * @throws DataAccessException if the User was not found
     */
    User getUser(String username) throws DataAccessException;
        /* Failures
        can't access database
        user not found
        */

    /**
     * Returns true if a User with the given username exists in the database.
     *
     * @param username the username of the User to fetch
     * @return true if the User was found, false otherwise
     * @throws DataAccessException if the User was not found
     */
    boolean hasUser(String username) throws DataAccessException;
        /* Failures
        can't access database
        */

    /**
     * Removes a single User from the database.
     *
     * @param user the User to remove
     */
    void removeUser(User user) throws DataAccessException;
        /* Failures
        can't access database
        (if user DNE, just return)
        */

    /**
     * Removes every User from the database.
     */
    void clearUsers() throws DataAccessException;
        /* Failures
        can't access database
        (if no users, just return)
        */
}
