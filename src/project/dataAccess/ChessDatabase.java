package dataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class ChessDatabase extends Database {

    public ChessDatabase() throws DataAccessException {
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
