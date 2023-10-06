package dataAccess;

import chess.ChessGame;
import models.Game;

import java.util.ArrayList;

/**
 * A DAO (Data Access Object) for CRUD operations on games currently being played.
 */
public class GameDAO {

    /**
     * Inserts a new {@link models.Game} into the database.
     *
     * @param game the game to insert
     * @throws DataAccessException if a game with the same ID already exists
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void insertNewGame(Game game) throws DataAccessException {
        /* Failures
        can't access database
        game already exists (same gameID)
        */
    }

    /**
     * Fetches the {@link models.Game} with the given ID from the database.
     *
     * @param gameID the ID of the {@code Game} to return
     * @return the fetched {@code Game}
     * @throws DataAccessException if a {@code Game} with the given ID was not found
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public Game findGame(int gameID) throws DataAccessException {
        /* Failures
        can't access database
        game not found
        */
        return null;
    }

    /**
     * Returns a list of all games currently in the database.
     *
     * @return a list of all games in the database
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public ArrayList<Game> allGames() throws DataAccessException {
        /* Failures
        can't access database
        */
        return null;
    }

    /**
     * Assigns a role to a user if not already assigned.
     *
     * @param gameID    the ID of the game to add the user to
     * @param username  the username of the user
     * @param roleColor the role to assign to the user
     * @throws DataAccessException if the game or the user was not found
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void assignPlayerRole(int gameID, String username, ChessGame.TeamColor roleColor)
            throws DataAccessException {
        /* Failures
        can't access database
        game not found
        user not found
        */

        /* TODO Not handled:
        role already claimed by different user (violation of SRP?)
        user already claimed different role? (just overwrite?) (violation of SRP?)
        (if user already has the role, just return.)
        */
    }

    /**
     * Updates the game state of a {@link models.Game} in the database to match the given version.
     *
     * @param game the updated version of the {@code Game}
     * @throws DataAccessException if no game with a matching gameID was found
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void updateGameSate(Game game) throws DataAccessException {
        /* Failures
        can't access database
        game not found
        */

        /* TODO Not handled:
        games don't match, i.e. different players (violation of SRP)
        */
    }

    /**
     * Removes a single game from the database.
     *
     * @param gameID the ID of the game to remove
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void removeGame(int gameID) throws DataAccessException {
        /* Failures
        can't access database
        (if game DNE, just return)
        */
    }

    /**
     * Removes every game from the database.
     *
     * @throws DataAccessException if there was a problem accessing the data store
     */
    public void clearGames() throws DataAccessException {
        /* Failures
        can't access database
        (if no games, just return)
        */
    }
}
