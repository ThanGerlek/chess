package dataAccess;

import chess.ChessGame;
import server.Game;
import server.http.GameListItem;

public class DatabaseGameDAO implements GameDAO {
    // TODO Write CREATE_GAME_TABLE
    private static final String CREATE_GAME_TABLE = """
            CREATE TABLE IF NOT EXISTS games (
                id INT NOT NULL AUTO_INCREMENT,
                PRIMARY KEY (id)
            )""";
    private final ChessDatabase database;
    private final UserDAO userDAO;

    public DatabaseGameDAO(ChessDatabase database, UserDAO userDAO) throws DataAccessException {
        this.userDAO = userDAO;
        this.database = database;
        database.executeSqlUpdate(CREATE_GAME_TABLE);
    }

    /**
     * Inserts a new Game into the database.
     *
     * @param game the Game to insert
     * @throws DataAccessException if a Game with the same gameID already exists
     */
    @Override
    public void insertNewGame(Game game) throws DataAccessException {

    }

    /**
     * Fetches the Game with the given ID from the database.
     *
     * @param gameID the ID of the {@code Game} to return
     * @return the fetched {@code Game}
     * @throws DataAccessException if a {@code Game} with the given ID was not found
     */
    @Override
    public Game findGame(int gameID) throws DataAccessException {
        return null;
    }

    /**
     * Returns an array containing data about each Game in the database.
     *
     * @return an array of data about each Game in the database
     */
    @Override
    public GameListItem[] allGames() throws DataAccessException {
        return new GameListItem[0];
    }

    /**
     * Assigns a role to a user if not already assigned.
     *
     * @param gameID   the ID of the game to add the user to
     * @param username the username of the user
     * @param role     the role to assign to the user
     * @throws DataAccessException if the game or the user was not found
     */
    @Override
    public void assignPlayerRole(int gameID, String username, ChessGame.PlayerRole role) throws DataAccessException {

    }

    /**
     * Updates the game state of a Game in the database to match the given version.
     *
     * @param game the updated version of the Game
     * @throws DataAccessException if no Game with a matching gameID was found
     */
    @Override
    public void updateGameState(Game game) throws DataAccessException {

    }

    /**
     * Removes a single game from the database.
     *
     * @param gameID the ID of the Game to remove
     */
    @Override
    public void removeGame(int gameID) throws DataAccessException {

    }

    /**
     * Removes every game from the database.
     */
    @Override
    public void clearGames() throws DataAccessException {

    }

    /**
     * Generates a new, unused gameID.
     *
     * @return a new gameID
     */
    @Override
    public int generateNewGameID() {
        return 0;
    }
}
