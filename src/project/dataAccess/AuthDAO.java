package dataAccess;

import server.AuthToken;

/**
 * A DAO (Data Access Object) for {@code AuthToken} objects and authentication data.
 */
public interface AuthDAO {

    //TODO Adding an authToken with the same string but a different username DOES WORK.
    //If it fails, now they know it's a valid token for someone else.

    /**
     * Registers the given {@code AuthToken} as a valid token.
     *
     * @param token the AuthToken to register
     * @throws DataAccessException if the AuthToken already exists or the user doesn't exist
     */
    void addAuthToken(AuthToken token) throws DataAccessException;
        /* Failures
        can't access database
        token already exists (incl. for different user)
        username doesn't exist
        */

    /**
     * Checks if the given {@code AuthToken} is currently valid.
     *
     * @param token the token to validate
     * @return true iff the given token is currently valid
     */
    boolean isValidAuthToken(AuthToken token) throws DataAccessException;
        /* Failures
        can't access database
        */

    /**
     * Invalidates the given {@code AuthToken}. Future calls requiring authorization for the given user will need to
     * generate a new token by re-authenticating.
     *
     * @param token the token to invalidate
     */
    void removeAuthToken(AuthToken token) throws DataAccessException;
        /* Failures
        can't access database
        (if token not found, just return)
        */

    /**
     * Invalidates every currently valid {@code AuthToken}. Future calls requiring authorization will need to generate
     * new tokens by re-authenticating.
     */
    void clearAuthTokens() throws DataAccessException;
        /* Failures
        can't access database
        (if no tokens, just return)
        */
}
