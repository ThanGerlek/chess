package dataAccess;

import server.User;

public class DatabaseUserDAO implements UserDAO {

    /**
     * Adds a new User to the database.
     *
     * @param user the User to insert
     * @throws DataAccessException if the username is already in the database
     */
    @Override
    public void insertNewUser(User user) throws DataAccessException {

    }

    /**
     * Gets the User with the given username from the database.
     *
     * @param username the username of the User to fetch
     * @return the fetched User
     * @throws DataAccessException if the User was not found
     */
    @Override
    public User getUser(String username) throws DataAccessException {
        return null;
    }

    /**
     * Returns true if a User with the given username exists in the database.
     *
     * @param username the username of the User to fetch
     * @return true if the User was found, false otherwise
     */
    @Override
    public boolean hasUser(String username) throws DataAccessException {
        return false;
    }

    /**
     * Removes a single user from the database.
     *
     * @param user the user to remove
     */
    @Override
    public void removeUser(User user) throws DataAccessException {

    }

    /**
     * Removes every user from the database.
     */
    @Override
    public void clearUsers() throws DataAccessException {

    }
}