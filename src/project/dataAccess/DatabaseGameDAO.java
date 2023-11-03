package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import server.Game;
import server.http.GameListItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                role VARCHAR(32) NOT NULL,
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
        database.update(CREATE_GAMES_TABLE);
        database.update(CREATE_ROLES_TABLE);
    }

    /**
     * Inserts a new Game into the database.
     *
     * @param game the Game to insert
     * @throws DataAccessException if a Game with the same gameID already exists
     */
    @Override
    public void insertNewGame(Game game) throws DataAccessException {
        // Failures: game already exists (same gameID)
        // TODO Test with Database
        memoryGameDAO.insertNewGame(game);

        String sqlString = "SELECT gameId FROM games WHERE gameId=?";
        boolean gameIdAlreadyExists = database.booleanQueryWithParam(sqlString, game.gameID());
        if (gameIdAlreadyExists) {
            throw new ValueAlreadyTakenException(
                    "Tried to insert a new Game with a gameID that already exists in the database");
        }

        String chessGameStr = new Gson().toJson(game.chessGame());

        database.update("INSERT INTO games (gameId, gameName, game) VALUES (?, ?, ?)", preparedStatement -> {
            preparedStatement.setInt(1, game.gameID());
            preparedStatement.setString(2, game.gameName());
            preparedStatement.setString(3, chessGameStr);
        });

        assignPlayerRolesFromGame(game); // TODO Is this redundant?
    }

    private void assignPlayerRolesFromGame(Game game) throws DataAccessException {
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

    private void assertIDExists(int gameID) throws DataAccessException {
        String sqlString = "SELECT gameId FROM games WHERE gameId=?";
        boolean gameIdExists = database.booleanQuery(sqlString, preparedStatement -> {
            preparedStatement.setInt(1, gameID);
        });

        if (!database.booleanQueryWithParam("SELECT gameId FROM games WHERE gameId=?", gameID)) {
            String msg = String.format("Tried to access a Game with an unrecognized gameID: '%d'", gameID);
            throw new NoSuchItemException(msg);
        }
    }

    private String roleToString(ChessGame.PlayerRole role) {
        return switch (role) {
            case WHITE_PLAYER -> "white";
            case BLACK_PLAYER -> "black";
            case SPECTATOR -> "spectator";
        };
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
        // Failures: game not found
        String sqlString = "SELECT game FROM games WHERE gameId=?";
        ArrayList<String> gameStrings = database.queryForString("game", sqlString, preparedStatement -> {
            preparedStatement.setInt(1, gameID);
        });

        if (gameStrings.isEmpty()) {
            String msg = String.format("Tried to access a Game with an unrecognized gameID: '%d'", gameID);
            throw new NoSuchItemException(msg);
        }

        // TODO! Adapter!!!
        return memoryGameDAO.findGame(gameID);
    }

    /**
     * Returns a list containing data about each Game in the database.
     *
     * @return a list of data about each Game in the database
     */
    @Override
    public ArrayList<GameListItem> allGames() throws DataAccessException {
        // TODO Implement with Database

        ArrayList<GameListItem> gameListItems = new ArrayList<>();


        String sqlString = """
                SELECT
                    games.gameName,
                    games.gameId,
                    MAX(CASE WHEN roles.role='white' THEN roles.username END) AS whiteUsername,
                    MAX(CASE WHEN roles.role='black' THEN roles.username END) AS blackUsername
                FROM games LEFT JOIN roles ON games.gameId=roles.gameId
                GROUP BY games.gameId
                """;
        Connection conn = database.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String gameName = rs.getString("gameName");
                    int gameID = rs.getInt("gameId");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    GameListItem item = new GameListItem(gameID, whiteUsername, blackUsername, gameName);
                    gameListItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to run executeQuery() on SQL String: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        } finally {
            database.returnConnection(conn);
        }

        return gameListItems;
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
        // Failures: game not found, user not found
        // TODO Test with Database
        memoryGameDAO.assignPlayerRole(gameID, username, role);

        assertIDExists(gameID);

        String sqlString = "SELECT id FROM users WHERE username=?";
        boolean userExists = database.booleanQueryWithParam(sqlString, username);
        if (!userExists) {
            throw new UnauthorizedAccessException("Unrecognized username");
        }

        database.update("INSERT INTO roles (username, role) VALUES (?, ?)", preparedStatement -> {
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
        // Failures: game not found
        // TODO Implement with Database
        assertIDExists(game.gameID());
        memoryGameDAO.updateGameState(game);

        String gameString = new Gson().toJson(game); // TODO! Adapter

        database.update("UPDATE games SET game=? WHERE gameId=?", preparedStatement -> {
            preparedStatement.setString(1, gameString);
            preparedStatement.setInt(2, game.gameID());
        });
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

        database.updateWithParam("DELETE FROM games WHERE gameId=?", gameID);
        database.updateWithParam("DELETE FROM roles WHERE gameId=?", gameID);
    }

    /**
     * Removes every game from the database.
     */
    @Override
    public void clearGames() throws DataAccessException {
        // TODO Implement with Database
        memoryGameDAO.clearGames();

        database.update("TRUNCATE games");
        database.update("TRUNCATE roles");
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
