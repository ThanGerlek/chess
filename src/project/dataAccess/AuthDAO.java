package dataAccess;

import server.AuthToken;

/**
 * A DAO (Data Access Object) for <code>AuthToken</code> objects and authentication data.
 */
public class AuthDAO {

    /**
     * Registers the given </code>AuthToken</code> as a valid token.
     *
     * @param token the AuthToken to register
     * @throws DataAccessException if the AuthToken already exists or the user doesn't exist
     */
    public void addAuthToken(AuthToken token) throws DataAccessException {
        /* Failures
        can't access database
        token already exists (incl. for different user)
        username doesn't exist
        */
    }

    /**
     * Checks if the given </code>AuthToken</code> is currently valid.
     *
     * @param token the token to validate
     * @return true iff the given token is currently valid
     */
    public boolean isValidAuthToken(AuthToken token) throws DataAccessException {
        /* Failures
        can't access database
        */
        return false;
    }

    /**
     * Invalidates the given </code>AuthToken</code>. Future calls requiring authorization for the given user will need
     * to generate a new token by re-authenticating.
     *
     * @param token the token to invalidate
     */
    public void removeAuthToken(AuthToken token) throws DataAccessException {
        /* Failures
        can't access database
        (if token not found, just return)
        */
    }

    /**
     * Invalidates every currently valid </code>AuthToken</code>. Future calls requiring authorization will need to
     * generate new tokens by re-authenticating.
     */
    public void clearAuthTokens() throws DataAccessException {
        /* Failures
        can't access database
        (if no tokens, just return)
        */
    }
}
