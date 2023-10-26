package dataAccess;

import chess.ChessGame;
import server.Game;
import server.http.GameListItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A DAO (Data Access Object) for CRUD operations on Games currently being played.
 */
public class MemoryGameDAO implements GameDAO {

    private static final Map<Integer, Game> gameDatabase = new HashMap<>();
    private static final Map<Integer, ArrayList<String>> spectatorDatabase = new HashMap<>();
    private static int maxUsedGameID;

    /**
     * Inserts a new Game into the database.
     *
     * @param game the Game to insert
     * @throws DataAccessException if a Game with the same gameID already exists
     */
    public void insertNewGame(Game game) throws DataAccessException {
        /* Failures
        can't access database
        game already exists (same gameID)
        */
        Integer id = game.gameID();
        if (gameDatabase.containsKey(id)) {
            throw new ValueAlreadyTakenException(
                    "Tried to insert a new Game with a gameID that already exists in the database");
        }
        gameDatabase.put(id, game);
        spectatorDatabase.put(id, new ArrayList<>());

        maxUsedGameID = Math.max(maxUsedGameID, game.gameID());
    }

    /**
     * Fetches the Game with the given ID from the database.
     *
     * @param gameID the ID of the Game to return
     * @return the Game with the given ID
     * @throws DataAccessException if a Game with the given ID was not found
     */
    public Game findGame(int gameID) throws DataAccessException {
        /* Failures
        can't access database
        game not found
        */
        assertIDExists(gameID);
        return gameDatabase.get(gameID);
    }

    /**
     * Returns an array containing data about each Game in the database.
     *
     * @return an array of data about each Game in the database
     */
    public GameListItem[] allGames() throws DataAccessException {
        /* Failures
        can't access database
        */
        Collection<GameListItem> gameList = new ArrayList<>();
        for (Game game : gameDatabase.values()) {
            gameList.add(new GameListItem(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return gameList.toArray(new GameListItem[0]);
    }

    /**
     * Assigns a role to a user if not already assigned.
     *
     * @param gameID   the ID of the game to add the user to
     * @param username the username of the user
     * @param role     the role to assign to the user
     * @throws DataAccessException if the game or the user was not found
     */
    public void assignPlayerRole(int gameID, String username, ChessGame.PlayerRole role) throws DataAccessException {
        /* Failures
        can't access database
        game not found
        user not found
        */
        // TODO
        assertIDExists(gameID);

        if (!(new MemoryUserDAO().hasUser(username))) {
            throw new NoSuchItemException("Unrecognized username");
        }

        switch (role) {
            case WHITE_PLAYER -> gameDatabase.get(gameID).setWhiteUsername(username);
            case BLACK_PLAYER -> gameDatabase.get(gameID).setBlackUsername(username);
            case SPECTATOR -> spectatorDatabase.get(gameID).add(username);
        }

        /* TODO Not handled:
        role already claimed by different user (violation of SRP?)
        user already claimed different role? (just overwrite?) (violation of SRP?)
        (if user already has the role, just return.)
        */
    }

    /**
     * Updates the game state of a Game in the database to match the given version.
     *
     * @param game the updated version of the Game
     * @throws DataAccessException if no Game with a matching gameID was found
     */
    public void updateGameState(Game game) throws DataAccessException {
        /* Failures
        can't access database
        game not found
        */
        assertIDExists(game.gameID());
        gameDatabase.put(game.gameID(), game);

        /* TODO Not handled:
        games don't match, i.e. different players (violation of SRP)
        */
    }

    /**
     * Removes a single game from the database.
     *
     * @param gameID the ID of the Game to remove
     */
    public void removeGame(int gameID) throws DataAccessException {
        /* Failures
        can't access database
        (if game DNE, just return)
        */
        gameDatabase.remove(gameID);
        spectatorDatabase.remove(gameID);
    }

    /**
     * Removes every game from the database.
     */
    public void clearGames() throws DataAccessException {
        /* Failures
        can't access database
        (if no games, just return)
        */
        gameDatabase.clear();
        spectatorDatabase.clear();
    }

    /**
     * Generates a new, unused gameID.
     *
     * @return a new gameID
     */
    public int generateNewGameID() {
        maxUsedGameID++;
        return maxUsedGameID;
    }

    private void assertIDExists(Integer gameID) throws DataAccessException {
        if (!gameDatabase.containsKey(gameID)) {
            throw new NoSuchItemException("Tried to access a Game with an unrecognized gameID");
        }
    }
}
