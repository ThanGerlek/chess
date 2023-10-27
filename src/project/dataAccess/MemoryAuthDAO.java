package dataAccess;

import server.AuthToken;

import java.util.ArrayList;
import java.util.Objects;

// TODO Update Javadocs with DataAccessException subclasses

/**
 * A DAO (Data Access Object) for {@code AuthToken} objects and authentication data.
 */
public class MemoryAuthDAO implements AuthDAO {

    private final ArrayList<AuthToken> tokenDatabase = new ArrayList<>();
    private final UserDAO userDAO;

    public MemoryAuthDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers the given {@code AuthToken} as a valid token.
     *
     * @param token the AuthToken to register
     * @throws DataAccessException if the AuthToken already exists or the user doesn't exist
     */
    public void addAuthToken(AuthToken token) throws DataAccessException {
        // Failures: can't access database; token already exists; username doesn't exist
        if (!userDAO.hasUser(token.username())) {
            throw new NoSuchItemException("User not found");
        }

        for (AuthToken existingToken : tokenDatabase) {
            if (Objects.equals(existingToken, token)) {
                throw new ValueAlreadyTakenException("Tried to register an already existing token");
            }
        }

        tokenDatabase.add(token);
    }

    /**
     * Checks if the given {@code AuthToken} is currently valid.
     *
     * @param token the token to validate
     * @return true iff the given token is currently valid
     */
    public boolean isValidAuthToken(AuthToken token) throws DataAccessException {
        // Failures: can't access database
        for (AuthToken existingToken : tokenDatabase) {
            if (existingToken == token) {
                return true;
            }
        }
        return false;
    }

    /**
     * Invalidates the given {@code AuthToken}. Future calls requiring authorization for the given user will need to
     * generate a new token by re-authenticating.
     *
     * @param token the token to invalidate
     * @throws DataAccessException if the token is invalid
     */
    public void removeAuthToken(AuthToken token) throws DataAccessException {
        // Failures: can't access database, invalid token
        // (no, it shouldn't be idempotent per the Phase 3 specs)
        if (isValidAuthToken(token)) {
            tokenDatabase.remove(token);
        } else {
            throw new DataAccessException("Couldn't invalidate token: the provided token is already invalid.");
        }
    }

    /**
     * Invalidates every currently valid {@code AuthToken}. Future calls requiring authorization will need to generate
     * new tokens by re-authenticating.
     */
    public void clearAuthTokens() throws DataAccessException {
        // Failures: can't access database (if no tokens, just return)
        tokenDatabase.clear();
    }
}
