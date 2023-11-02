package dataAccess;

import server.AuthToken;

public class DatabaseAuthDAO implements AuthDAO {
    private static final String CREATE_AUTH_TABLE = """
            CREATE TABLE IF NOT EXISTS auth (
                id INT NOT NULL AUTO_INCREMENT,
                token VARCHAR(128) NOT NULL UNIQUE,
                username VARCHAR(128) NOT NULL,
                PRIMARY KEY (id),
                UNIQUE INDEX (token)
            )""";
    private final ChessDatabase database;
    private final UserDAO userDAO;
    private final MemoryAuthDAO memoryAuthDAO; // TODO Remove

    public DatabaseAuthDAO(ChessDatabase database, UserDAO userDAO) throws DataAccessException {
        this.userDAO = userDAO;
        this.database = database;
        database.executeSqlUpdate(CREATE_AUTH_TABLE);
        memoryAuthDAO = new MemoryAuthDAO(userDAO);
    }

    /**
     * Registers the given {@code AuthToken} as a valid token.
     *
     * @param token the AuthToken to register
     * @throws DataAccessException if the AuthToken already exists or the user doesn't exist
     */
    @Override
    public void addAuthToken(AuthToken token) throws DataAccessException {
        // TODO Implement with Database
        memoryAuthDAO.addAuthToken(token);
    }

    /**
     * Checks if the given {@code AuthToken} is currently valid.
     *
     * @param token the token to validate
     * @return true iff the given token is currently valid
     * @throws DataAccessException if there was a problem accessing the data store
     */
    @Override
    public boolean isValidAuthToken(String token) throws DataAccessException {
        // TODO Implement with Database
        return memoryAuthDAO.isValidAuthToken(token);
    }

    /**
     * Invalidates the given {@code AuthToken}. Future calls requiring authorization for the given user will need to
     * generate a new token by re-authenticating.
     *
     * @param token the token to invalidate
     */
    @Override
    public void removeAuthToken(String token) throws DataAccessException {
        // TODO Implement with Database
        memoryAuthDAO.removeAuthToken(token);
    }

    /**
     * Invalidates every currently valid {@code AuthToken}. Future calls requiring authorization will need to generate
     * new tokens by re-authenticating.
     */
    @Override
    public void clearAuthTokens() throws DataAccessException {
        // TODO Implement with Database
        memoryAuthDAO.clearAuthTokens();
    }

    /**
     * Returns the username corresponding to the given token.
     *
     * @param authToken the token to look up
     * @return the username corresponding to the token
     * @throws DataAccessException if the token is invalid
     */
    @Override
    public String getUsername(String authToken) throws DataAccessException {
        // TODO Implement with Database
        return memoryAuthDAO.getUsername(authToken);
    }
}


