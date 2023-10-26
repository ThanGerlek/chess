package dataAccess;

import server.AuthToken;

import java.util.ArrayList;

/**
 * A DAO (Data Access Object) for {@code AuthToken} objects and authentication data.
 */
public class MemoryAuthDAO implements AuthDAO {

    private static final ArrayList<AuthToken> tokenDatabase = new ArrayList<>();

    /**
     * Registers the given {@code AuthToken} as a valid token.
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
        // TODO
    }

    /**
     * Checks if the given {@code AuthToken} is currently valid.
     *
     * @param token the token to validate
     * @return true iff the given token is currently valid
     */
    public boolean isValidAuthToken(AuthToken token) throws DataAccessException {
        /* Failures
        can't access database
        */
        // TODO
        return false;
    }

    /**
     * Invalidates the given {@code AuthToken}. Future calls requiring authorization for the given user will need to
     * generate a new token by re-authenticating.
     *
     * @param token the token to invalidate
     */
    public void removeAuthToken(AuthToken token) throws DataAccessException {
        /* Failures
        can't access database
        (if token not found, just return)
        */
        // TODO
    }

    /**
     * Invalidates every currently valid {@code AuthToken}. Future calls requiring authorization will need to generate
     * new tokens by re-authenticating.
     */
    public void clearAuthTokens() throws DataAccessException {
        /* Failures
        can't access database
        (if no tokens, just return)
        */
        // TODO
    }
}
