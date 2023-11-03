package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class ChessDatabase extends Database {

    public void executeSqlUpdate(String sqlString) throws DataAccessException {
        executeSqlUpdate(sqlString, sp -> {
        });
    }

    public void executeSqlUpdate(String sqlString, StatementPreparer sp) throws DataAccessException {
        Connection conn = getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlString)) {
            sp.prepare(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to run executeUpdate() on SQL String: `" + sqlString + "`");
            throw new DataAccessException(e.getMessage());
        } finally {
            returnConnection(conn);
        }
    }
}