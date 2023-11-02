package dataAccess;

import chess.ChessGame;
import server.Game;
import server.http.GameListItem;

public class DatabaseGameDAO implements GameDAO {
    private static final String CREATE_GAMES_TABLE = """
            CREATE TABLE IF NOT EXISTS games (
                gameId INT NOT NULL AUTO_INCREMENT,
                gameName VARCHAR(256),
                game TEXT NOT NULL,
                PRIMARY KEY (gameId)
            )""";
    private static final String CREATE_ROLES_TABLE = """
            CREATE TABLE IF NOT EXISTS roles (
                id INT NOT NULL AUTO_INCREMENT,
                gameId INT NOT NULL,
                username VARCHAR(128) NOT NULL,
                role INT NOT NULL,
                PRIMARY KEY (id),
                INDEX (gameId)
            )""";
    private final ChessDatabase database;
    private final UserDAO userDAO;
    MemoryGameDAO memoryGameDAO; // TODO Remove

    public DatabaseGameDAO(ChessDatabase database, UserDAO userDAO) throws DataAccessException {
        this.userDAO = userDAO;
        this.database = database;
        database.executeSqlUpdate(CREATE_GAMES_TABLE);
        database.executeSqlUpdate(CREATE_ROLES_TABLE);
        memoryGameDAO = new MemoryGameDAO(userDAO);
    }

    /**
     * Inserts a new Game into the database.
     *
     * @param game the Game to insert
     * @throws DataAccessException if a Game with the same gameID already exists
     */
    @Override
    public void insertNewGame(Game game) throws DataAccessException {
        // TODO Implement with Database
        memoryGameDAO.insertNewGame(game);
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
        // TODO Implement with Database
        return memoryGameDAO.findGame(gameID);
    }

    /**
     * Returns an array containing data about each Game in the database.
     *
     * @return an array of data about each Game in the database
     */
    @Override
    public GameListItem[] allGames() throws DataAccessException {
        // TODO Implement with Database
        return memoryGameDAO.allGames();
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
        // TODO Implement with Database
        memoryGameDAO.assignPlayerRole(gameID, username, role);
    }

    /**
     * Updates the game state of a Game in the database to match the given version.
     *
     * @param game the updated version of the Game
     * @throws DataAccessException if no Game with a matching gameID was found
     */
    @Override
    public void updateGameState(Game game) throws DataAccessException {
        // TODO Implement with Database
        memoryGameDAO.updateGameState(game);

    }

    /**
     * Removes a single game from the database.
     *
     * @param gameID the ID of the Game to remove
     */
    @Override
    public void removeGame(int gameID) throws DataAccessException {
        // TODO Implement with Database
        memoryGameDAO.removeGame(gameID);
    }

    /**
     * Removes every game from the database.
     */
    @Override
    public void clearGames() throws DataAccessException {
        // TODO Implement with Database
        memoryGameDAO.clearGames();
    }

    /**
     * Generates a new, unused gameID.
     *
     * @return a new gameID
     */
    @Override
    public int generateNewGameID() {
        // TODO Implement with Database
        return memoryGameDAO.generateNewGameID();
    }
}
