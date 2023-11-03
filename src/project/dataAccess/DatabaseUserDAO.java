package dataAccess;

import server.User;

import java.sql.Types;

public class DatabaseUserDAO implements UserDAO {
    private static final String CREATE_USER_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(128) NOT NULL,
                password VARCHAR(128) NOT NULL,
                email VARCHAR(128),
                PRIMARY KEY (id),
                UNIQUE INDEX (username)
            )""";
    private final ChessDatabase database;
    MemoryUserDAO memoryUserDAO; // TODO Remove

    public DatabaseUserDAO(ChessDatabase database) {
        this.database = database;
        memoryUserDAO = new MemoryUserDAO();
    }

    /**
     * Set up this UserDAO.
     */
    @Override
    public void initialize() throws DataAccessException {
        database.update(CREATE_USER_TABLE);
    }

    /**
     * Adds a new User to the database.
     *
     * @param user the User to insert
     * @throws DataAccessException if the username is already in the database
     */
    @Override
    public void insertNewUser(User user) throws DataAccessException {
        // Failures: username already exists
        // TODO Test with Database
        memoryUserDAO.insertNewUser(user);

        database.update("INSERT INTO users (username, password, email) VALUES (?, ?, ?)",
                preparedStatement -> {
                    preparedStatement.setString(1, user.username());
                    preparedStatement.setString(2, user.password());
                    if (user.email() == null || user.email().isEmpty()) {
                        preparedStatement.setNull(3, Types.VARCHAR);
                    } else {
                        preparedStatement.setString(3, user.email());
                    }
                });
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
        // Failures: user not found
        // TODO Implement with Database
        return memoryUserDAO.getUser(username);
    }

    /**
     * Returns true if a User with the given username exists in the database.
     *
     * @param username the username of the User to fetch
     * @return true if the User was found, false otherwise
     */
    @Override
    public boolean hasUser(String username) throws DataAccessException {
        // TODO Implement with Database
        return memoryUserDAO.hasUser(username);
    }

    /**
     * Removes a single user from the database.
     *
     * @param user the user to remove
     */
    @Override
    public void removeUser(User user) throws DataAccessException {
        // TODO Test with Database
        memoryUserDAO.removeUser(user);

        database.updateWithParam("DELETE FROM users WHERE username=?", user.username());
    }

    /**
     * Removes every user from the database.
     */
    @Override
    public void clearUsers() throws DataAccessException {
        // TODO Implement with Database
        memoryUserDAO.clearUsers();

        database.update("TRUNCATE users");
    }
}
