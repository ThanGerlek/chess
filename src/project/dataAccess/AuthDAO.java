package dataAccess;

import models.AuthToken;

/**
 * A DAO (Data Access Object) for {@link models.AuthToken} objects and authentication data.
 */
public class AuthDAO {

    /**
     * Registers the given {@link models.AuthToken} as a valid token.
     *
     * @param token the {@code AuthToken} to register
     * @throws DataAccessException if the {@code AuthToken} already exists or the user doesn't exist
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void addAuthToken(AuthToken token) throws DataAccessException {
        /* Failures
        can't access database
        token already exists (incl. for different user)
        username doesn't exist
        */
    }

    /**
     * Checks if the given {@link models.AuthToken} is currently valid.
     *
     * @param token the token to validate
     * @return true iff the given token is currently valid
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public boolean isValidAuthToken(AuthToken token) throws DataAccessException {
        /* Failures
        can't access database
        */
        return false;
    }

    /**
     * Invalidates the given {@link models.AuthToken}. Future calls requiring authorization for the given user will need
     * to generate a new token by re-authenticating.
     *
     * @param token the token to invalidate
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void removeAuthToken(AuthToken token) throws DataAccessException {
        /* Failures
        can't access database
        (if token not found, just return)
        */
    }

    /**
     * Invalidates every currently valid {@link models.AuthToken}. Future calls requiring authorization will need to
     * generate new tokens by re-authenticating.
     *
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void clearAuthTokens() throws DataAccessException {
        /* Failures
        can't access database
        (if no tokens, just return)
        */
    }
}
