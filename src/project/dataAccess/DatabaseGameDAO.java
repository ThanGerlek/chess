package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
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

    public DatabaseGameDAO(ChessDatabase database, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.database = database;
        memoryGameDAO = new MemoryGameDAO(userDAO);
    }

    /**
     * Set up this GameDAO.
     */
    @Override
    public void initialize() throws DataAccessException {
        database.executeSqlUpdate(CREATE_GAMES_TABLE);
        database.executeSqlUpdate(CREATE_ROLES_TABLE);
    }

    /**
     * Inserts a new Game into the database.
     *
     * @param game the Game to insert
     * @throws DataAccessException if a Game with the same gameID already exists
     */
    @Override
    public void insertNewGame(Game game) throws DataAccessException {
        // TODO Test with Database
        memoryGameDAO.insertNewGame(game);

        String chessGameStr = new Gson().toJson(game.chessGame());

        database.executeSqlUpdate("INSERT INTO games (gameId, gameName, game) VALUES (?, ?, ?)", preparedStatement -> {
            preparedStatement.setInt(1, game.gameID());
            preparedStatement.setString(2, game.gameName());
            preparedStatement.setString(3, chessGameStr);
        });

        // TODO Is this redundant?
        if (!game.whiteUsername().isEmpty()) {
            assignPlayerRole(game.gameID(), game.whiteUsername(), ChessGame.PlayerRole.WHITE_PLAYER);
        }
        if (!game.blackUsername().isEmpty()) {
            assignPlayerRole(game.gameID(), game.whiteUsername(), ChessGame.PlayerRole.BLACK_PLAYER);
        }
        for (String spectator : game.getSpectators()) {
            assignPlayerRole(game.gameID(), spectator, ChessGame.PlayerRole.SPECTATOR);
        }
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
        // TODO Test with Database
        memoryGameDAO.assignPlayerRole(gameID, username, role);

        database.executeSqlUpdate("INSERT INTO roles (username, role) VALUES (?, ?)", preparedStatement -> {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, roleToString(role));
        });
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
        // TODO Test with Database
        memoryGameDAO.removeGame(gameID);

        database.executeSqlUpdate("DELETE FROM games WHERE gameId=?", preparedStatement -> {
            preparedStatement.setInt(1, gameID);
        });
        database.executeSqlUpdate("DELETE FROM roles WHERE gameId=?", preparedStatement -> {
            preparedStatement.setInt(1, gameID);
        });
    }

    /**
     * Removes every game from the database.
     */
    @Override
    public void clearGames() throws DataAccessException {
        // TODO Implement with Database
        memoryGameDAO.clearGames();

        database.executeSqlUpdate("TRUNCATE games");
        database.executeSqlUpdate("TRUNCATE roles");
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

    private String roleToString(ChessGame.PlayerRole role) {
        return switch (role) {
            case WHITE_PLAYER -> "white";
            case BLACK_PLAYER -> "black";
            case SPECTATOR -> "spectator";
        };
    }

    private ChessGame.PlayerRole stringToRole(String roleString) {
        if ("white".equals(roleString)) {
            return ChessGame.PlayerRole.WHITE_PLAYER;
        } else if ("black".equals(roleString)) {
            return ChessGame.PlayerRole.BLACK_PLAYER;
        } else if ("spectator".equals(roleString)) {
            return ChessGame.PlayerRole.SPECTATOR;
        } else {
            String msg = String.format("Called stringToRole() with an unrecognized role string: '%s'", roleString);
            throw new IllegalArgumentException(msg);
        }
    }
}
