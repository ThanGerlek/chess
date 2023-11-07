package dataAccess;

import chess.ChessGame;
import chess.ChessGameImpl;
import server.ChessSerializer;
import server.Game;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseGameFinder {
    private final Database database;

    public DatabaseGameFinder(Database database) {
        this.database = database;
    }

    public Game find(int gameID) throws DataAccessException {
        GameQueryResult gameQueryResult = queryForGame(gameID);
        String gameName = gameQueryResult.gameName();
        String chessGameJson = gameQueryResult.chessGameJson();

        ChessGame chessGame = ChessSerializer.gson().fromJson(chessGameJson, ChessGameImpl.class);

        Game game = new Game(gameID, gameName, chessGame);

        ArrayList<RoleQueryResult> roleQueryResults = queryForRoles(gameID);
        parseRolesIntoGameObject(game, roleQueryResults);

        return game;
    }

    private GameQueryResult queryForGame(int gameID) throws DataAccessException {
        String sqlString = "SELECT gameName, game FROM games WHERE gameId=?";
        GameQueryResult result;

        Connection conn = database.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            preparedStatement.setInt(1, gameID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    String msg = String.format("Tried to access a Game with an unrecognized gameID: '%d'", gameID);
                    throw new NoSuchItemException(msg);
                }
                String gameName = rs.getString(1);
                String chessGameJson = rs.getString(2);
                result = new GameQueryResult(gameName, chessGameJson);
            }

        } catch (SQLException e) {
            System.out.println("Failed to run executeQuery() on SQL String: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        } finally {
            database.returnConnection(conn);
        }

        return result;
    }

    private ArrayList<RoleQueryResult> queryForRoles(int gameID) throws DataAccessException {
        String sqlString = "SELECT username, role FROM roles WHERE gameId=?";
        ArrayList<RoleQueryResult> results = new ArrayList<>();

        Connection conn = database.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            preparedStatement.setInt(1, gameID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString(1);
                    String role = rs.getString(2);
                    results.add(new RoleQueryResult(username, role));
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to run executeQuery() on SQL String: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        } finally {
            database.returnConnection(conn);
        }
        return results;
    }

    private void parseRolesIntoGameObject(Game game, ArrayList<RoleQueryResult> roleQueryResults) {
        for (RoleQueryResult roleResult : roleQueryResults) {
            String username = roleResult.username();
            PlayerRole role = PlayerRole.stringToRole(roleResult.role());

            if (role.equals(PlayerRole.WHITE_PLAYER)) {
                game.setWhiteUsername(username);
            } else if (role.equals(PlayerRole.BLACK_PLAYER)) {
                game.setBlackUsername(username);
            } else {
                game.addSpectator(username);
            }
        }
    }

    record GameQueryResult(String gameName, String chessGameJson) {
    }

    record RoleQueryResult(String username, String role) {
    }
}
