package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class ChessDatabase extends Database {
    private static final String CREATE_DATABASE_SQL_STRING =
            "CREATE DATABASE IF NOT EXISTS chess CHARACTER SET utf8mb4";

    public ChessDatabase() throws DataAccessException {
        initialize();
    }

    private void initialize() throws DataAccessException {
        try {
            executeSqlUpdate(CREATE_DATABASE_SQL_STRING);
        } catch (DataAccessException e) {
            throw new DataAccessException("Failed to initialize database with error: " + e.getMessage());
        }
    }

    public void executeSqlUpdate(String sqlString) throws DataAccessException {
        Connection conn = getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to run executeUpdate() on SQL String: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        } finally {
            returnConnection(conn);
        }
    }


}
